
<p align="center">
    <img src="https://oldcheatbreaker.com/assets/images/CheatBreaker.gif" width="150" height="150"/>
</p>

# CheatBreakerAPI

This fork of the CheatBreakerAPI will allow you to detect when a player is running CheatBreaker+, enable and disable Staff Modules, create custom server themes, and much more.

## Reporting an issue

If you suspect an issue you can submit one [here](https://github.com/CheatBreakerPlus/CheatBreakerAPI/issues).

## Get the Source

1. Install maven `sudo apt-get install maven`
2. Verify installation `mvn -v`
3. Clone the repository `git clone https://github.com/CheatBreakerPlus/CheatBreakerAPI.git`
4. Navigate to the new folder `cd CheatBreakerAPI`
5. Import `pom.xml` into your IDE

## Compile a Build

1. Navigate to the repository home directory
2. Run `mvn clean install`
3. Find the compiled jar at `target/CheatBreakerAPI.jar`

## Contributing
You can submit a [pull request](https://github.com/CheatBreakerPlus/CheatBreakerAPI/pulls) with your changes.

# Features

## Server Themes

Server Themes are a completely new feature that has the ability to change how CheatBreaker+ looks entirely. 

This feature can only be shown to the player when they have their Color Theme option set to "Server".

## How it works
It works the exact same way you'd send any other packet to the client.

### How to send this packet correctly
When sending a packet, make sure you define the colors correctly, you may use Integers, Hex, or RGB values.

For example, on player login, you'd have this:

**Please note: this example was introduced as a test packet and is not visually pleasing, the sole purpose is for you to fine tune colors to your liking.**

`CheatBreakerAPI.getInstance().sendPacket(player, new CBPacketServerTheme(Color.BLACK.getRGB(), Color.GREEN.getRGB(), Color.BLUE.getRGB(), Color.MAGENTA.getRGB(), Color.GREEN.getRGB(), Color.RED.getRGB()));`
