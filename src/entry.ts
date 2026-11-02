// The bundle's entry (build.sh): the js-module's exports are the API itself, so that Cordova's
// clobbers makes it window.MRZScanner.
import { MRZScanner } from './index';

declare const module: { exports: unknown };
module.exports = MRZScanner;
