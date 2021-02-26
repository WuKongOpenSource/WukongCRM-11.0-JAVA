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

  window.onload = function() {
    var n = document.createElement('div')
    n.style.position = 'fixed'
    n.style.bottom = '2px'
    n.style.left = '0'
    n.style.right = '0'
    n.style.textAlign = 'center'
    n.style.fontSize = '12px'
    n.style.color = '#999'
    n.style.zIndex = 1

    var i = document.createElement('img')
    i.style.width = '14px'
    i.style.verticalAlign = 'bottom'
    i.style.marginRight = '5px'

    var img = new Image()
    img.onload = function() {
      var canvas = document.createElement('canvas')
      var ctx = canvas.getContext('2d')

      canvas.height = img.naturalHeight
      canvas.width = img.naturalWidth
      ctx.drawImage(img, 0, 0)

      var uri = canvas.toDataURL('image/png')
      i.src = uri
      n.appendChild(i)
      var ti = ''
      var tis = [80, 111, 119, 101, 114, 32, 98, 121, 32]
      tis.forEach(c => {
        ti = ti + String.fromCharCode(c)
      })
      var t = document.createTextNode(ti + decodeURI('%E6%82%9F%E7%A9%BA'))
      n.appendChild(t)
      document.body.appendChild(n)
    }
    img.src = 'static/img/logo.png'
  }

  if (Sys.appName === 'IE' && Number(Sys.version) < 10) {
    window.location.href = './static/index.html'
  }
})();
