# StickyBot Discord Bot

[www.stickybot.info](https://www.stickybot.info/)

[![Discord Bots](https://top.gg/api/widget/status/628400349979344919.svg)](https://top.gg/bot/628400349979344919)
[![Discord Bots](https://top.gg/api/widget/servers/628400349979344919.svg)](https://top.gg/bot/628400349979344919)
[![Discord Bots](https://top.gg/api/widget/owner/628400349979344919.svg)](https://top.gg/bot/628400349979344919)


![StickyBot Logo](https://images.discordapp.net/avatars/628400349979344919/b2aed74a6631ee9755a8ae56d8e582a8.png?size=512=250x)

StickBot is a Discord bot that uses Discords REST API, Gradle, and JDA4. The bot lets a user "stick" a message to a channel, meaning that stickied message will always be the most recent message in the channel even when other users send a message. The bot connects to a mySQL database to save information so it will keep data after a restart or update. The bot has many other command, some of which use other APIs.

**Add StickyBot to your server [here](https://top.gg/bot/628400349979344919).**

*(NOTE: Not all source code is up-to-date of what is running on the live server for StickyBot. Some files have also been modified for privacy and security reasons.)*

## Self-Hosting

This bot is **not** made to be self-hosted, it is on GitHub for transparency purposes.

Self-hosting your own copy of this bot is **not** supported; the source code is provided here so users and other bot developers can see how the bot functions. No support will be provided for editing, compiling, or building any code in this repository, and any changes must be documented as per the license.

If you would like to self-host you will need to change these values in Main.java:

```java
public static String botId = "Your Bot ID";
public static String token = "Your Bot Token";
public static String dbUrl = "Your MySQL DB Address";
public static String dbUser = "Your MySQL DB Username";
public static String dbPassword = "Your MySQL DB Password";
```

No other support will be available for self-hosting of this bot.

## Usage

To get a list of commands use the `?help` command. 


## Contributing
For major changes, please open an issue first to discuss what you would like to change, *or* contact me on Discord: P_O_G#2222 *or* join the [StickBot Support Server](https://discord.gg/SvNQTtf) on Discord.


## License
Apache License 2.0

Copyright 2020 Cameron Smith

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
