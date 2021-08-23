var login = {
    init : function () {
        var _this = this;
        $('#login-btn').on('click', function () {
            _this.login();
        });
        $('#join-btn').on('click', function () {
            _this.join();
        });
    },
    login: function () {
        var data = {
            id: $('#id').val(),
            passwd: $('#passwd').val()
        };

        $.ajax({
            type: 'POST',
            url: '/login',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('login 되었습니다');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    join: function () {
        var data = {
            username: $('#id').val(),
            password: $('#passwd').val()
        };
        console.log(data);
        console.log(JSON.stringify(data));

        $.ajax({
            type: 'POST',
            url: '/join',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('회원가입 되었습니다');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

login.init();
