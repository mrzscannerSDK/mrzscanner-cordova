import Foundation

/// The MRZ scanner for Cordova: each action passes its string arguments to the SDK's MRZScan
/// (compiled into the app with this file) and returns its JSON (platforms/API.md) as a string;
/// www/mrzscanner.js parses it.
@objc(MRZScannerCordova)
class MRZScannerCordova: CDVPlugin {

    @objc(registerLicense:)
    func registerLicense(_ command: CDVInvokedUrlCommand) {
        send(command, MRZScan.registerLicense(arg(command, 0)))
    }

    @objc(version:)
    func version(_ command: CDVInvokedUrlCommand) {
        send(command, MRZScan.version())
    }

    @objc(scanImage:)
    func scanImage(_ command: CDVInvokedUrlCommand) {
        let image = arg(command, 0), options = arg(command, 1)
        commandDelegate.run {
            self.send(command, MRZScan.scanImage(image, options: options))
        }
    }

    @objc(scan:)
    func scan(_ command: CDVInvokedUrlCommand) {
        let settings = arg(command, 0)
        DispatchQueue.main.async {
            MRZScan.present(from: self.viewController, settings: settings) { self.send(command, $0) }
        }
    }

    private func arg(_ command: CDVInvokedUrlCommand, _ i: Int) -> String {
        i < command.arguments.count ? command.arguments[i] as? String ?? "" : ""
    }

    private func send(_ command: CDVInvokedUrlCommand, _ json: String) {
        commandDelegate.send(CDVPluginResult(status: .ok, messageAs: json), callbackId: command.callbackId)
    }
}
