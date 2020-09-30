!(function () {
  var Sys = {};
  var ua = navigator.userAgent.toLowerCase();

  var regMap = {
    msie: '/msie ([\\d.]+)/',
    firefox: '/firefox\\/([\\d.]+)/',
    chrome: '/chrome\\/([\\d.]+)/',
    opera: '/opera.([\\d.]+)/',
    safari: '/version\\/([\\d.]+).*safari/'
  };

  var o;
  if (ua.match(eval(regMap.msie))) {
    o = ua.match(eval(regMap.msie));
    Sys = {
      appName: 'IE',
      version: o[1]
    }
  } else if (ua.match(eval(regMap.firefox))) {
    o = ua.match(eval(regMap.firefox));
    Sys = {
      appName: 'Firefox',
      version: o[1]
    }
  } else if (ua.match(eval(regMap.chrome))) {
    o = ua.match(eval(regMap.chrome));
    Sys = {
      appName: 'Chrome',
      version: o[1]
    }
  } else if (ua.match(eval(regMap.opera))) {
    o = ua.match(eval(regMap.opera));
    Sys = {
      appName: 'Opera',
      version: o[1]
    }
  } else if (ua.match(eval(regMap.safari))) {
    o = ua.match(eval(regMap.safari));
    Sys = {
      appName: 'Safari',
      version: o[1]
    }
  } else if (ua.indexOf('edge')) {
    Sys = {
      appName: 'Edge'
    }
  } else {
    if (navigator.appName === 'Microsoft Internet Explorer') {
      Sys = {
        appName: 'IE',
        version: '6.0'
      }
    }
  }

  if (Sys.appName === 'IE' && Number(Sys.version) < 10) {
    window.location.href = './static/index.html'
  }
})();
