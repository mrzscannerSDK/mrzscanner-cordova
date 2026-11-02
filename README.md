# @mrzscanner/cordova

Reads the machine-readable zone (MRZ) of passports, ID cards and visas, offline: a full-screen
camera with AR mode (a box follows the MRZ and shows the read), or a photo. cordova-android 13+
(Android 7+, API 24) and cordova-ios 7+ (iOS 15+).

```
cordova plugin add @mrzscanner/cordova
```

iOS: set the deployment target to 15.0 in your app's `config.xml` (Xcode 27 refuses cordova-ios's
default of 13.0 for any app):

```xml
<platform name="ios">
    <preference name="deployment-target" value="15.0" />
</platform>
```

The camera usage text is set for you; change it with
`cordova plugin add @mrzscanner/cordova --variable CAMERA_USAGE_DESCRIPTION="..."`.

## Use

After `deviceready`, the plugin is `window.MRZScanner`:

```js
await MRZScanner.registerLicense('MRZ2...');      // once; without it some fields are masked

const result = await MRZScanner.scan({ documentTypes: ['passport', 'id'] });
if (result.status === 'read') {
  const read = result.reads[0];
  console.log(read.text, read.fields.document_number, read.checkDigitsValid);
}

const fromPhoto = await MRZScanner.scanImage({ base64: jpegBase64 });   // or { path }
```

`scan(settings)` opens the camera (asking for the permission), and closes after the first read, or
with `continuous: true` collects each new document until Stop. It resolves with
`{ status, reads, elapsedMs }`; `status` is `read`, `stopped`, `timeout`, `permissionDenied`,
`licenseInactive` or `error`. Settings: `continuous`, `duplicateTimeoutMs`, `confirmation`
(`normal` / `fast`), `timeoutMs`, `torch`, `zoom`, `vibrateOnRead`, `arOverlay`,
`documentTypes`, `regionOfInterest`, `isolateMrz`, `isolatedHeight`, `title`.

Each read: `text` (the MRZ lines), `checkDigitsValid`, `kind`, `fields` (`document_number`,
`surnames`, `given_names`, `nationality`, `dob_raw`, `expiration_date_raw`, ...), `corners`,
`isolatedMrz`. TypeScript: `www/mrzscanner.d.ts` declares `MRZScanner` and the types.

`scanImage(image, options)`: `image` is `{ path }` (file path or URI) or `{ base64 }` (JPEG / PNG);
`options`: `{ documentTypes, isolatedHeight }`. Resolves with the read or `null`.

Version 2.7.4. Licence: a commercial MRZ scanner licence (https://mrzscanner.com/).
