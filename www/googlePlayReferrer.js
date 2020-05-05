var exec = require("cordova/exec");

function getReferrer(success, error) {
  exec(success, error, "GooglePlayReferrer", "getReferrer");
}

exports.getReferrer = getReferrer;

// function GooglePlayReferrer() {}

// // The function that passes work along to native shells
// GooglePlayReferrer.prototype.getReferrer = function (
//   successCallback,
//   errorCallback
// ) {
//   console.log("get referrer plugin called: ");
//   // if (!successCallback) {
//   //   return new Promise((resolve, reject) => {
//   //     this.getReferrer(resolve, reject);
//   //   });
//   // }

//   exec(successCallback, errorCallback, "GooglePlayReferrer", "getReferrer", [
//     {
//       instance: "",
//     },
//   ]);
// };

// // Installation constructor that binds ToastyPlugin to window
// GooglePlayReferrer.install = function () {
//   if (!window.plugins) {
//     window.plugins = {};
//   }
//   window.plugins.googlePlayReferrer = new GooglePlayReferrer();
//   return window.plugins;
// };
// cordova.addConstructor(GooglePlayReferrer.install);
