# SFTHBot

This is a bot I coded for the SFTH fancord improv comedy server.

## Running the bot for yourself
The current code of this bot is set up to only let me register new servers, this is because the bot is hosted locally and I can't afford to pay for a stronger VPS to put it public.
Interestingly, the bot does support multiple servers via the mongo db, but you'll need to add code to create the `Server` class on join.
Also, you will need to create a `.env` in the same directory as the bot's JAR file that contains the following.
```
TOKEN= <Bot Token>
MONGO_URI= <Mongo Database URI>
GPT_KEY= <OpenAI GPT Key>
```