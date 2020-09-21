Introduction
============
Haxe extension for Google License verification.

More info about Google License Verification Library: https://developer.android.com/google/play/licensing


Platforms
=========
Android (with Google Play)


Installation
=======
You can easily install Google LVL extension using haxelib:

	haxelib install extension-lvl

To add it to a Haxe project, add this to your project file:

	<haxelib name="extension-lvl" />


Usage
=======

Add the following line with Base64-encoded RSA public key from your Google Play Console app's page (Development tools -> Services and APIs) to your **project.xml** file:
```xml
<set name="LVL_PUBLIC_KEY" value="[YOUR_APP_PUBLIC_KEY]" />
```

```haxe
import extension.lvl.ExtensionLVL;

...

var res:Int = ExtensionLVL.isLicensed();
if(res == ExtensionLVL.LICENSED)
{
	//app is licensed, continue
}
else if(res == ExtensionLVL.NOT_LICENSED)
{
	//show licensing dialog, redirect to Google Play, close app, whatever
}
```

License
=======
Google License Verification extension is free, open-source software under the [MIT license](LICENSE.md).