

print('Script start!');

var LIMIT = 2;

for (var i = 0; i < LIMIT; i++) {
    print('Script works!' + i);
}

// var result = Framework.runScriptFile("script.js");

for (var i = 0; i < LIMIT; i++) {
    print('Script works!' + i);
}

while (true) {
    var name = Framework.readLine();
    if (name.indexOf("exit") === 0) { break
    } else if (name.indexOf("run ") === 0) { Framework.runScript(name.substring(4));
    } else {
        print("> " + name);
    }
}