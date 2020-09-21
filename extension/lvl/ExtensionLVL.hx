package extension.lvl;

import lime.system.JNI;

/**
 * Google Play License Verification extension
 * @author Pozirk Games (https://www.pozirk.com)
 */
class ExtensionLVL
{
	//policy codes, in Google Play Console go to Settings -> Developer account -> Account details to play with License Test Resposnse
	public static inline var LICENSED:Int = 0x0100; //you get this reposnse even on LICENSED_OLD_KEY, which is not a big deal, I guess
	public static inline var NOT_LICENSED:Int = 0x0231;
	public static inline var RETRY:Int = 0x0123; //most likely will happen when there is no internet connection or problem with Google servers, app needs to be restarted to retry
	
	public static inline var ERROR_NO_RESULT:Int = -1; //didin't get result from lvl yet, which is strange
	//> app error codes
	public static inline var ERROR_INVALID_PACKAGE_NAME:Int = 1;
	public static inline var ERROR_NON_MATCHING_UID:Int = 2;
	public static inline var ERROR_NOT_MARKET_MANAGED:Int = 3;
	public static inline var ERROR_CHECK_IN_PROGRESS:Int = 4;
	public static inline var ERROR_INVALID_PUBLIC_KEY:Int = 5;
	public static inline var ERROR_MISSING_PERMISSION:Int = 6;
	//<
	
	public static function isLicensed():Int
	{
#if android
		return extension_lvl_isLicensed();
#else
		return ERROR_NO_RESULT;
#end
	}
	
#if android
	private static var extension_lvl_isLicensed = JNI.createStaticMethod ("org.haxe.extension.ExtensionLVL", "isLicensed", "()I");
#end
}