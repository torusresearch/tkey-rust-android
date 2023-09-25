# tkey Android SDK

The tKey SDK manages private keys by generating shares of it using Shamir Secret Sharing. Multi-party computation of shares is also planned for this SDK and is currently in an advanced stage of development but not quite ready to be merged into the main codebase yet.

The companion example application is [here](https://github.com/torusresearch/tkey-rust-android-example/).

If you are instead seeking the iOS implementation, please see [here](https://github.com/torusresearch/tkey-rust-ios/).

## Including this package

1. Compile the project 

2. Import the built AAR file into your application.

## Building and viewing the documentation

```terminal
./gradlew javadoc && open ./tkey/build/docs/javadoc/index.html
```

## Integration with CustomAuth

This standalone package can be used with CustomAuth. Please refer to the example application for a more comprehensive overview of the code.

The integration process is simple:
1. Log in with CustomAuth, for detailed documentation on how to do this please refer to the documentation [here](https://github.com/torusresearch/customauth-android-sdk).

2. Use the methods `getPrivateKey().toString(16)` on the `TorusLoginResponse` object as the postbox key when setting up the ServiceProvider.

3. Continue initialization as normal.

4. Remember to save the device share or the account will need to be reset. For existing accounts, shares will need to be imported, either by making use of a security share or via manual entry depending on how the ThresholdKey was initially setup.

## SDK Design Overview

The design of the SDK is relatively straight forward. 

* It makes use of the underlying NDK build which contains binaries for arm64-v8, armeabi-v7a, x86 and x86_64 architectures. 
* These binaries are built from a common cross-compilable codebase which is a native port of the original implementation of (tkey)[https://github.com/tkey/tkey]. 
* All marshalling of types across the foreign function interface and JNI as well as memory safety is handled by the SDK.

This SDK can be split into methods which are synchronous and methods which are asynchronous:
* Methods which are synchronous are expected to return immediately with no underlying network operations.
* Methods which are asynchronous are dispatched to a single-threaded Executor, return the result via callbacks and perform network operations with the Metadata Server. The relevant network implementation is already supplied as part of the SDK.

All classes that are part of the modules namespace are static in nature and can only operate on a ThresholdKey object which has been properly setup.

Currently only the Secp256k1 curve is supported.

Please note that all code examples are minimalistic in nature, this is intentionally done for clarity, since most functions can throw.

## SDK Overview

### ThresholdKey

The instance of tkey, this can be considered the most important object in the SDK. 

##### Creation

To create a ThresholdKey object at minimum a StorageLayer is required, however it is more practical to use a ServiceProvider as well.

```java
	PrivateKey postboxKey = PrivateKey.generate();
    StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
    ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
    ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
```

#### Initialization

Once you have created a ThresholdKey object, it can then be initialized.

A KeyDetails object is returned from the initialization call.

```java
     PrivateKey key = PrivateKey.generate();
     thresholdKey.initialize(key.hex, null, false, false, result -> {
     	if (result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
        	// Error handling here
        } else if (result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Success) {
        	KeyDetails details = ((Result.Success<KeyDetails>) result).data;
        }
     });
```

Additionally the following optional parameters can be supplied to this call
| Parameter | Type | Description |
| --------- | ---- | ----------- |
| import_share | String | Initialise tkey with an existing share. This allows you to directly initialise tKey without using the service provider login. |
| input | ShareStore | Import an existing ShareStore into tkey. | 

#### Reconstructing the Private Key

Once the required number of shares are available to the ThresholdKey object or existing shares have been inserted into it, the private key can then be reconstructed.

This method returns a KeyReconstructionResult.

```java
		thresholdKey.reconstruct(reconstruct_result -> {
        	if (reconstruct_result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
                     //Error handling here                  
           } else if (reconstruct_result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Success) {
           		KeyReconstructionDetails reconstructionDetails = ((com.web3auth.tkey.ThresholdKey.Common.Result.Success<KeyReconstructionDetails>) reconstruct_result).data;
           }
      });                   
```

#### Getting the key details.

```java
    KeyDetails details = thresholdKey.getKeyDetails();
```

This returns a KeyDetails object.

Whenever a method is called which affects the state of the ThresholdKey, this method will need to be called again if updated details of the ThresholdKey is needed.


#### Generating a new Share

Shares are generated on the same threshold (e.g, 2/3 -> 2/4). A GenerateShareStoreResult object is returned by the function. 

```java
   thresholdKey.generateNewShare(result -> {
       if (result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
           // Error handling here
       } else if (result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Success) {
           GenerateShareStoreResult shareStoreResult = ((com.web3auth.tkey.ThresholdKey.Common.Result.Success<GenerateShareStoreResult>) result).data;
       }
    });
```

#### Deleting a Share

Shares can be deleted by their share index. Note that deleting a share will invalidate any persisted share.

```java
    thresholdKey.deleteShare(index, result -> {
        if (result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
            // Error handling here
        } else if (result instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Success) {
            // Success here, void return type
        }
    });
```

### Modules for additional functionality

For more advanced operations on a ThresholdKey object, you can make use of the provided modules.

#### PrivateKeysModule

This module provides an interface for setting, getting and managing private keys for a ThresholdKey object.

#### SecurityQuestionModule

This module allows the creation of a security share with a password. This is particularly useful to recover a ThresholdKey 

#### SeedPhraseModule

This module provides functionality for setting, changing, getting, and deleting seed phrases for a ThresholdKey object.

#### ShareSerializationModule

The ShareSerializationModule allows the serialization and deserialization of shares between mnemonic and hex formats.

#### ShareTransferModule

The ShareTransferModule is used to transferring an existing share to another device.
