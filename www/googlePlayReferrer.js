var exec = require("cordova/exec");

function getReferrer(success, error) {
  if (!success) {
    return new Promise(function (resolve, reject) {
      getReferrer(resolve, reject);
    });
  }

  exec(
    function success(data) {
      console.log("success ", data);
      if (ref) {
        success(ref);
      } else {
        error("No referrer data");
      }
    },
    error,
    "GooglePlayReferrer",
    "getReferrer"
  );
}

exports.getReferrer = getReferrer;
