# CryptoCraft

CryptoCraft is a economy plugin based on crypto currencies. It takes information from an API about the crypto currencies in real time. The data is updated every hour or everytime you enable the plugin. 

## Commands

### User Commands

- `/bal - See your current balance.`
- `/cbal - See your crypto coins data.`
- `/coin <command> - Buy, sell and list all the available crypto coins.`
- `/pay <Player> <Quantity> - Transfer money to another player.`
- `/cpay <Player> <Coin> <Quantity ($)> - Transfer money by crypto coins to another player.`

### Admin Commands

- `/crypto <command> - Manage the economy. Set balance, give and remove coins from players.`


## Configuration

### User Data (users.yml)

This file stores the data of players. It is not recommended to edit unless you know what you're doing. Looks like this:
```yaml
24733cde-0497-4c37-bebd-c51411afc2b1:
  name: UnluckyGermi
  balance: 123.2
  coins:
    BTC: 0.0024015160285176416
```

### Plugin Config (config.yml)
- `starting-money - Set the amount of money a player will receive on their first login (Default: 0).`
- `currency-symbol - Set the currency symbol (Default: '$').`
