// Empty constructor
function GooglePlayReferrer() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
GooglePlayReferrer.prototype.getReferrer = function (
  successCallback,
  errorCallback
) {
  // console.log("get referrer plugin called");
  // if (!successCallback) {
  //   return new Promise((resolve, reject) => {
  //     this.getReferrer(resolve, reject);
  //   });
  // }

  cordova.exec(
    successCallback,
    errorCallback,
    "GooglePlayReferrer",
    "getReferrer",
    []
  );
};

// Installation constructor that binds ToastyPlugin to window
GooglePlayReferrer.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.googlePlayReferrer = new GooglePlayReferrer();
  return window.plugins.googlePlayReferrer;
};
cordova.addConstructor(GooglePlayReferrer.install);
