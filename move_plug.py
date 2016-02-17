#!/usr/bin/python
# -*- coding: utf-8 -*-

import sys
import os
import shutil
try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET

def replace(fileName, replaceDic):
    keys = replaceDic.keys()
    if len(keys) == 0:
        return
    f = open(fileName, 'r')
    s = f.read()
    for key in keys:
        s = s.replace(key, replaceDic[key])
    f.close()
    f = open(fileName, 'w')
    f.write(s)
    f.close()
    
def readPropertiesFile(properties, file, splitkey):
    f = open(file, 'r')
    s = f.readline()
    while s:
        keys = s.split(splitkey)
        if keys and len(keys) == 2:
            properties[keys[0].strip()] = keys[1].strip()
        s = f.readline()
    f.close()
    return properties

def readManifestProperties(properties, file):
    tree = ET.ElementTree(file=file)
    android = "{http://schemas.android.com/apk/res/android}"
    for attrib in tree.getroot().attrib:
        if attrib.endswith("versionName"):
            properties["versionName"] = tree.getroot().attrib[attrib]
        elif attrib.endswith("versionCode"):
            properties["versionCode"] = tree.getroot().attrib[attrib]
        elif attrib.endswith("package"):
            properties["package"] = tree.getroot().attrib[attrib]
    for elem in tree.iter():
        if elem.tag == "meta-data":
            properties[elem.attrib[android + "name"]] = elem.attrib[android + "value"]
    return      
def genManifestReplaceDic(manifestProperties, manifestReplaceDic, key, oldvalue):
    if manifestProperties.has_key(key):
        manifestReplaceDic["android:value=\"" + oldvalue + "\""] = "android:value=\"" + manifestProperties[key] + "\""
    
def moveSrcProject(src, dst):
    names = os.listdir(src)
    keyProperties = {}
    buildGradleReplaceDic = {}
    manifestProperties = {}
    manifestReplaceDic = {}
    for name in names:
        srcname = os.path.join(src, name)
        if os.path.isdir(srcname):
            if name == "libs":
                shutil.copytree(srcname, os.path.join(dst, name))
            elif name=="libs_ex":
                os.makedirs(os.path.join(dst, "libs_ex"))
                srcfiles = os.listdir(srcname)
                for srcfile in srcfiles:
                    if not srcfile.startswith("android-support") and not srcfile=="plug_lib.jar":
                        shutil.copyfile(os.path.join(src, name,srcfile),os.path.join(dst, "libs_ex",srcfile))
            elif name == "res" or name == "java" or name == "assets"or name == "aidl":
                shutil.copytree(srcname, os.path.join(dst, "src", "main", name))
            elif name == "src":
                srcfiles = os.listdir(srcname)
                for srcfile in srcfiles:
                    srcfilepath = os.path.join(src, name,srcfile)
                    if(srcfile=="main"):
                        moveSrcProject(srcname, dst)
                        break
                    elif os.path.isdir(srcfilepath):
                        shutil.copytree(srcfilepath, os.path.join(dst, "src", "main","java",srcfile))
            elif name == "com":
                shutil.copytree(srcname, os.path.join(dst, "src", "main","java", name))
            else:
                moveSrcProject(srcname, dst)
        elif name == "AndroidManifest.xml":
            readManifestProperties(manifestProperties, srcname)
        elif name == "project.properties":
            readPropertiesFile(keyProperties, srcname, "=")
    
    if keyProperties.has_key("key.store"):
       keyfile = keyProperties["key.store"].replace("./", "")
       buildGradleReplaceDic["keystore/key.keystore"] = keyfile
       dstkeyfile = os.path.join(dst, keyfile)
       os.makedirs(os.path.dirname(dstkeyfile))
       shutil.copyfile(os.path.join(src, keyfile), dstkeyfile)
       buildGradleReplaceDic["storePassword \'mihome\'"] = "storePassword \'mihome\'".replace("mihome", keyProperties["key.store.password"])
       buildGradleReplaceDic["keyAlias \'mihome-demo-key\'"] = "keyAlias \'mihome-demo-key\'".replace("mihome-demo-key", keyProperties["key.alias"])
       buildGradleReplaceDic["keyPassword \'mihome\'"] = "keyPassword \'mihome\'".replace("mihome", keyProperties["key.alias.password"])
    if manifestProperties.has_key("versionCode"):
        buildGradleReplaceDic["versionCode 1"] = "versionCode " + manifestProperties["versionCode"]
    if manifestProperties.has_key("versionName"):
        buildGradleReplaceDic["versionName \"1.0\""] = "versionName \"" + manifestProperties["versionName"] + "\""
    if manifestProperties.has_key("package"):
        buildGradleReplaceDic["applicationId \"com.xiaomi.demo\""] = "applicationId \"" + manifestProperties["package"] + "\""
        manifestReplaceDic["package=\"com.xiaomi.demo\""] = "package=\"" + manifestProperties["package"] + "\""
#   


    genManifestReplaceDic(manifestProperties, manifestReplaceDic, "minPluginSdkApiVersion", "2")
    genManifestReplaceDic(manifestProperties, manifestReplaceDic, "model", "xiaomi.demo.v1") 
    genManifestReplaceDic(manifestProperties, manifestReplaceDic, "message_handler", "com.xiaomi.demo.MessageReceiver") 
    genManifestReplaceDic(manifestProperties, manifestReplaceDic, "MiHomeDeveloperId", "id_894148746") 
    genManifestReplaceDic(manifestProperties, manifestReplaceDic, "MiHomePlatform", "phone") 
    genManifestReplaceDic(manifestProperties, manifestReplaceDic, "MiHomeSupportWidget", "false") 
    
                    
    replace(os.path.join(dst, "build.gradle"), buildGradleReplaceDic)
    replace(os.path.join(dst, "src", "main", "AndroidManifest.xml"), manifestReplaceDic)
    

def movePoject(src, dst, base):
    dst = os.path.join(dst, os.path.basename(src))
    print dst
    if os.path.exists(dst):
        shutil.rmtree(dst)
    os.mkdir(dst)
    shutil.copy(os.path.join(base, "build.gradle"), dst)
    shutil.copy(os.path.join(base, ".gitignore"), dst)
    shutil.copy(os.path.join(base, "proguard-rules.pro"), dst)
    os.makedirs(os.path.join(dst, "src", "main"))
    shutil.copy(os.path.join(base, "src", "main", "AndroidManifest.xml"), os.path.join(dst, "src", "main"))
    moveSrcProject(src, dst)

if len(sys.argv)==2:
    movePoject(sys.argv[1],"plugProject", "Demo")
else:
    print "error no old plug project path,for example:python move_plug.py oldPlugPath"

def moveProjectDir(src,dst,base):
    names = os.listdir(src)
    for name in names:
        srcname = os.path.join(src, name)
        if os.path.isdir(srcname):
            print srcname
            movePoject(srcname,dst,base)
    
# moveProjectDir("/Users/livy/Documents/MI/SmartHome/android/old",
#            "plugProject", "Demo")