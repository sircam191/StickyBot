# StickyBot Discord Bot

StickBot is a Discord bot that uses Discords REST API, Gradle, and JDA4. The bot lets a user "stick" a message to a channel, meaning that stickied message will always be the most recent message in the channel even when other users send a message. The bot connect to a mySQL database to save information so it will keep data after a restart or update.

Add StickyBot to your server [here](https://top.gg/bot/628400349979344919).

## Installation

This bot is not made to be self-hosted, it is on GitHub for transparency purposes. If you would like to self-host you will need to change these values in Main.java:

```java
public static String botId = "Your Bot ID";
public static String token = "Your Bot Token";
```

No other support will be available for self-hosting of this bot.

## Usage

To get a list of commands use the `?help` command. 


## Contributing
For major changes, please open an issue first to discuss what you would like to change, or contact me on Discord: P_O_G#2222 or join the [StickBot Support Server](https://discord.gg/SvNQTtf).


## License
MIT License

Copyright (c) 2020 Cameron Smith

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
