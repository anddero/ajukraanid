var vm = new Vue({
    el: "#game",
    data: {
        tabNr: 0,
        theRandomNumber: "",
        newCode: "",
        newName: "",
        items: [
        ],
    },
    methods: {
        addPlayersToList: function(word) {
            this.items = word;
            console.log(this.items);
        },
        getPlayers: function() {
            var makeAjaxDos = setInterval(function() {
                var myObject = new Object();
                myObject.Action = "FetchState";
                myObject.Code = document.getElementById("gamenumber").innerHTML;
                console.log(myObject);
                fetch('http://localhost:8080', {
                    method: 'post',
                    headers: {
                        'Accept': 'application/json, text/plain, */*',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(myObject)              
                }).then(res=>res.json())
                .then(res =>
                {
                    var word1 = res.Data.substring(1, res.Data.length - 1);
                    vm.addPlayersToList(word1.split(" ").join("").split(","));
                    if (vm.items.length == 5) {
                        clearInterval(makeAjaxDos);
                    }
                })
                ;
            }, 5000);
        },
        startGame: function(){
            var myObject = new Object();
            myObject.Action = "JoinGame";
            myObject.Code = document.getElementById("code").value;
            myObject.Name = document.getElementById("name").value;
            document.getElementById("gamenumber").innerHTML = document.getElementById("code").value;
            fetch('http://localhost:8080', {
                method: 'post',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(myObject)
            }).then(res=>res.json())
            .then(res =>
            {
                console.log(res);
                if (res.State == "Error") {
                    this.tabNr = 4;
                } else {
                    var playerName = document.getElementById("name").value;
                    this.tabNr = 5;
                    console.log(playerName);
                    this.getPlayers();
                }
            })
            ;
        },
        playTheGame: function() {
            this.tabNr = 1;
        },
        switchTab: function(a) { 
            this.tabNr = a;
        },
        makeTheGame: function() {
            this.tabNr = 2;
            var randomCode = "";
            var myObject = new Object();
            myObject.Action = "CreateGame";
            var dataToBePosted = JSON.stringify(myObject);
            var http = new XMLHttpRequest();
            var url = "http://localhost:8080/";
            http.onreadystatechange = function() {
                if (http.readyState == 4 && http.status == 200) {
                    var code = JSON.parse(http.responseText).Code;
                    this.theRandomNumber = code;
                    document.getElementById("gamenumber").innerHTML = code;
                    document.getElementById("forRandomNumber").innerHTML = code;
                }
            }
            http.open("POST", url, true);
            http.setRequestHeader("Content-type", "application/json; charset=utf-8");
            http.send(dataToBePosted);
        }
    }
});
