
var file;
var url = "http://localhost:8082/v1/match/employees"
document.getElementById("uploadedFile").addEventListener('change', function (ev) {
    file = ev.target.files[0];
    document.getElementById("sendFile").disabled = false
})

var myList = [
    { "name": "abc", "age": 50 },
    { "age": "25", "hobby": "swimming" },
    { "name": "xyz", "hobby": "programming" }
];
document.getElementById("sendFile").addEventListener("click", function (e) {


    buildHtmlTable(document.getElementById("result"))

    var formData = new FormData();
    formData.append('file', file)
    fetch(url, {
        method: 'POST',
        body: formData
    }).then(function (response) {
        return response.json()
    }).then(function (data) {
        //document.getElementById("result").innerHTML = JSON.stringify(data, undefined, 2)
        document.getElementById("score").innerHTML = "Average score = " + data["avgScore"]
        document.getElementById("result").innerHTML = JSON.stringify(data["listOfMatches"], null, 2)
    })
} )

function buildHtmlTable(selector) {
    var columns = addAllColumnHeaders(myList, selector);

    for (var i = 0; i < myList.length; i++) {
        var row$ = $('<tr/>');
        for (var colIndex = 0; colIndex < columns.length; colIndex++) {
            var cellValue = myList[i][columns[colIndex]];
            if (cellValue == null) cellValue = "";
            row$.append($('<td/>').html(cellValue));
        }
        $(selector).append(row$);
    }
}
function addAllColumnHeaders(myList, selector) {
    var columnSet = [];
    var headerTr$ = $('<tr/>');

    for (var i = 0; i < myList.length; i++) {
        var rowHash = myList[i];
        for (var key in rowHash) {
            if ($.inArray(key, columnSet) == -1) {
                columnSet.push(key);
                headerTr$.append($('<th/>').html(key));
            }
        }
    }
    $(selector).append(headerTr$);

    return columnSet;
}