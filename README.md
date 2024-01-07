# SimpleFriends
A simple friends plugin I made in a few days.

[![Java CI with Maven](https://github.com/gwerrry/SimpleFriends/actions/workflows/maven.yml/badge.svg)](https://github.com/gwerrry/SimpleFriends/actions/workflows/maven.yml)

## Features
- ```SQLite database```
- ```Yaml Config for customizing messages, database paramaters, and the command itself```
- ```Friend leave and join messages```
- ```Add, remove, and list friends```

## Commands
You can customize the "/friend" command itself and the aliases. See the [config.yml](https://github.com/gwerrry/SimpleFriends/blob/0b0e0e4649cc6b63be2b20f1fae0a1d3f07036ca/src/main/resources/config.yml) "friend_cmd" section for a more clear understanding of what is customizable.
- ```/friend help``` | Displays the help text
- ```/friend invite <player>``` | Sends a friend invite to the specified player. Only works if they are online
- ```/friend kick <player>``` | Removes the specified player from your friends list
- ```/friend accept <player>``` | Accepts friend invite from player
- ```/friend deny <player>``` | Denys friend invite from player
- ```/friend list``` | Lists your friends

## Config
Take a look at the [config.yml](https://github.com/gwerrry/SimpleFriends/blob/0b0e0e4649cc6b63be2b20f1fae0a1d3f07036ca/src/main/resources/config.yml). Almost everything can be customized.    
Keep in mind this does not support using "&" for colors. You must use "§".

## Notes
- This is intended and only tested for version `1.20.4`
- Since I only did this in a few days and was feeling a little sick, the design is a little off and some of the algorithms are not optimized.
- I'm happy with this fun little project and it most likely will not get any updates.
