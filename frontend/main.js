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
        startGame: function(){
            var myObject = new Object();
            myObject.Action = "JoinGame";
            myObject.Code = document.getElementById("code").value;
            myObject.Name = document.getElementById("name").value;
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
                    this.items.push({name: playerName});
                    this.tabNr = 5;
                    console.log(playerName);
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
                    document.getElementById("forRandomNumber").innerHTML = JSON.parse(http.responseText).Code;
                }
            }
            http.open("POST", url, true);
            http.setRequestHeader("Content-type", "application/json; charset=utf-8");
            http.send(dataToBePosted);
        }
    }
});