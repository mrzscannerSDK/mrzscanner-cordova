// The Cordova plugin's JavaScript (bundled into www/mrzscanner.js by build.sh, clobbering
// window.MRZScanner): the shared API over cordova.exec, strings in, JSON out.
import { createApi } from './mrzscanner';

declare const cordova: {
  exec(ok: (r: string) => void, fail: (e: unknown) => void, service: string, action: string, args: string[]): void;
};

export * from './mrzscanner';

export const MRZScanner = createApi(
  (method, args) => new Promise<string>((resolve, reject) => cordova.exec(resolve, reject, 'MRZScanner', method, args)),
);
