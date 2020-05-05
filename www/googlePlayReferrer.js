function GooglePlayReferrer() {}

// The function that passes work along to native shells
GooglePlayReferrer.prototype.getReferrer = function (
  successCallback,
  errorCallback
) {
  console.log("get referrer plugin called: ", cordova.exec);
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
    [
      {
        instance: "",
      },
    ]
  );
};

// Installation constructor that binds ToastyPlugin to window
GooglePlayReferrer.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.googlePlayReferrer = new GooglePlayReferrer();
  return window.plugins;
};
cordova.addConstructor(GooglePlayReferrer.install);
