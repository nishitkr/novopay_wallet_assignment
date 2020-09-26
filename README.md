# novopay_wallet_assignment

## Requirements

User Wallet system where a user can come and register for a wallet. User will be able to make transactions using wallet. Write APIs for

1. Create a User Account - Sign-in/Sign-up
2. Add Money To Wallet
3. Transferring money from one wallet to another
4. Compute charges and commission
5. Do status inquiry of a transaction
6. Reversal of a transaction
7. View Passbook - All Transactions with all legs of a transaction of the user

Note: Both charge and commission can be applicable to either DEBIT or CREDIT party based on the input.
Charge - 0.2% of the Transaction Amount
Commission - 0.05% of the Transaction Amount     

### Limitations
1. For login, username and password need to be sent as request url parameters
2. Spring security not used

## API Endpoints

1. **Signup** <br/>
    POST    http://localhost:8080/novopay/api/v1/user/signup <br/>
    Body: <br/>
    {<br/>
      "username" : "\<username\>", <br/>
      "name" : "\<name\>", <br/>
      "address" : "\<address\>", <br/>
      "email" : "\<email\>", <br/>
      "phone" : "\<phone\>", <br/>
      "password" : "\<password\>" <br/>
    }

2. **Add money to wallet** <br/>
    POST    http://localhost:8080/novopay/api/v1/transact/addmoney?user=<username\>&pwd=<password\> <br/>
    Body : <br/>
    { <br/>
      "amount" : "\<amount\>" <br/>
    }
    
3. **Transfer money to other user's wallet** <br/>
    POST    http://localhost:8080/novopay/api/v1/transact/transfer?user=<username\>&pwd=<password\> <br/>
    Body : <br/>
    { <br/>
      "recipientUsername" : "\<username\>", <br/>
      "amount" : "\<amount\>" <br/>
    }
    
 4. **Transaction Status Inquiry** <br/> 
    GET   http://localhost:8080/novopay/api/v1/transact/statusinquiry?id=<transaction_uuid\> <br/>
    
 5. **View Passbook**  <br/>
    GET   http://localhost:8080/novopay/api/v1/user/passbook?user=<username\>&pwd=<password\> <br/>
    
 6. **Reverse Transaction** <br/>
    **Note: This request can only be triggered by Admin** <br/>
    POST    http://localhost:8080/novopay/api/v1/transact/reverse?user=admin&pwd=admin&id=<transaction_uuid\> <br/>
