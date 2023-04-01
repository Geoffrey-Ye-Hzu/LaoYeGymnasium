function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}

function registerApi(data) {
    return $axios({
        'url': '/user/register',
        'method': 'post',
        data
    })
}

function userIdApi() {
    return $axios({
        'url': '/user',
        'method': 'get',
    })
}

//根据手机号获取后端生成的验证码
function sendMsgApi(data) {
    return $axios({
        'url': '/user/sendMsg',
        'method': 'post',
        data
    })

}

  