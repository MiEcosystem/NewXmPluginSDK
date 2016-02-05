#!/usr/bin/env bash

PLUGIN_SDK_PATH="NewXmPluginSDK"
if [ ! -d "$PLUGIN_SDK_PATH" ]
then
	 echo Pull the sdk
	 git clone https://github.com/MiEcosystem/NewXmPluginSDK.git

else
	cd "$PLUGIN_SDK_PATH"
	echo Pull the sdk down to the latest
	git add . && git stash && git stash drop
	git pull
fi