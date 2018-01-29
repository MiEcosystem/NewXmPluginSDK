#!/usr/bin/python
# -*- coding: utf-8 -*-

import sys
import os
import shutil


def replace(fileName, replaceDic):
    f = open(fileName, 'r')
    s = f.read()
    keys = replaceDic.keys()
    for key in keys:
        s = s.replace(key, replaceDic[key])
    f.close()

    f = open(fileName, 'w')
    f.write(s)
    f.close()


def replaceDir(dir, replaceDic):
    names = os.listdir(dir)
    for name in names:
        if name == 'key.keystore':
            continue
        srcname = os.path.join(dir, name)
        if os.path.isdir(srcname):
            replaceDir(srcname, replaceDic)
        else:
            replace(srcname, replaceDic)


def copyfile(src, dst):
    names = os.listdir(src)
    for name in names:
        srcname = os.path.join(src, name)
        dstname = os.path.join(dst, name)
        shutil.copy(srcname, dstname)


#
def gen_plug(modelName, developerId, packageName):
    modelParam = modelName.split('.')
    if len(modelParam) != 3:
        print 'model format error, for example: xiaomi.demo.v1'
        return
    projectName = modelParam[0] + '_' + modelParam[1]
    originProjectName = 'EmptyDemo'
    originProjectPath = os.getcwd() + '/plugProject/' + originProjectName
    projectPath = os.getcwd() + '/plugProject/' + projectName
    if os.path.exists(projectPath):
        shutil.rmtree(projectPath)
    shutil.copytree(originProjectPath, projectPath)
    if packageName != 'com.xiaomi.demo':
        shutil.rmtree(projectPath + '/src/main/java/')
        dstSrcPath = projectPath + '/src/main/java/' + packageName.replace('.', '/')
        os.makedirs(dstSrcPath)
        copyfile(originProjectPath + '/src/main/java/com/xiaomi/demo', dstSrcPath)

    replaceDic = {'EmptyDemo': projectName, 'com.xiaomi.demo': packageName, 'xiaomi.demo.v1': modelName,
                  'id_894148746': 'id_' + developerId}
    replaceDir(projectPath, replaceDic)
    print 'gen plug ' + projectName + ' success'


isTest = False
# if __name__ == "__main__":
if isTest:
    modelName = 'xiaomi.camera.v1'
    developerId = '894148746'
    packageName = 'com.xiaomi.demo'
    gen_plug(modelName, developerId, packageName)

else:
    if len(sys.argv) < 4:
        print "params error,for example: python gen_plug xiaomi.demo.v1 894148746 com.xiaomi.demo"
    else:
        modelName = sys.argv[1]
        developerId = sys.argv[2]
        packageName = sys.argv[3]
        gen_plug(modelName, developerId, packageName)
