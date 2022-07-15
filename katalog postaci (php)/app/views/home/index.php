<html lang="pl">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Katalog postaci</title>
    <link rel="stylesheet" href="./style.css">
    <!-- <link rel="icon" href="./favicon.ico" type="image/x-icon"> -->
</head>

<body>

    <a href="/public"><h1>Katalog Postaci</h1></a>

    <main>

        <div id="view">
            <?php if ($data["view"]) { ?>

                <img src="<?= $data["view"]["image"] ?>" alt="<?= ucfirst($data["view"]["name"]) ?>">

                <div class="description">

                    <p>
                        <?php
                        if (isset($data["view"]["color"])) {
                            echo '<font color="' . $data["view"]["color"] . '">';
                        } else {
                            echo '<b>';
                        }
                        echo ucfirst($data["view"]["name"]);
                        if (isset($data["view"]["color"])) {
                            echo '</font>&nbsp;' . substr($data["view"]["color"], 1);
                        } else {
                            echo '</b>';
                        }
                        ?>
                        <button class="copy" onclick="copy(event, this)">
                            <span>Copy</span>
                            <img src="/app/images/copy.png" alt="copy" /></button>
                    </p>

                    <?php
                        echo '<p class="big">';
                        echo preg_replace('/(^|\s)([^\s]{1,20}:)/', '<b>$2</b>', $data["view"]["appearance"]);
                        echo '</p>';
                    ?>

                    <p>
                        <b>Siła:</b><?= $data["view"]["strength"] ?>
                        <b>Wytrz:</b><?= $data["view"]["endurance"] ?>
                        <b>Szybk:</b><?= $data["view"]["speed"] ?>
                        <b>Reak:</b><?= $data["view"]["reaction"] ?>
                        <?php
                        if ($data["view"]["magic_energy_control"] != "") {
                            echo " <b>KEM:</b> " . $data["view"]["magic_energy_control"];
                        }
                        if ($data["view"]["concentration"] != "") {
                            echo " <b>Konc:</b> " . $data["view"]["concentration"];
                        }
                        ?>
                    </p>

                    <?php
                    if ($data["view"]["magic"] != "") {
                        echo '<p class="big">';
                        echo preg_replace('/(^|\s)([^\s]{1,20}:)/', '<b>$2</b>', $data["view"]["magic"]);
                        echo '</p>';
                    }
                    ?>

                    <?php
                    echo '<textarea class="code" readonly>';
                    echo '[Tabela]&#10';
                    echo '[Tr]&#10';
                    echo '[Th=30%][img]' . $data["view"]["image"] . '[/img][/Th]&#10';
                    echo '[Td]&#10';
                    echo '[b]Imię:[/b] ';
                    if (isset($data["view"]["color"])) {
                        echo '[color=' . $data["view"]["color"] . ']';
                    }
                    echo $data["view"]["name"];
                    if (isset($data["view"]["color"])) {
                        echo '[/color] ' . substr($data["view"]["color"], 1);
                    }
                    echo '&#10';
                    echo '[align=justify]';
                    echo preg_replace('/(^|\s)([^\s]{1,20}:)/', '[b]$2[/b]', $data["view"]["appearance"]) . '&#10';
                    echo '[/align]';
                    echo 'S: ' . $data["view"]["strength"] . ' W: ' . $data["view"]["endurance"];
                    echo ' Sz: ' . $data["view"]["speed"] . ' R: ' . $data["view"]["reaction"];
                    if ($data["view"]["magic_energy_control"] != "") {
                        echo " KEM: " . $data["view"]["magic_energy_control"];
                    }
                    if ($data["view"]["concentration"] != "") {
                        echo " K: " . $data["view"]["concentration"];
                    }
                    echo '&#10';
                    if ($data["view"]["magic"] != "") {
                        echo preg_replace('/(^|\s)([^\s]{1,20}:)/', '[b]$2[/b]', $data["view"]["magic"]) . '&#10';
                    }
                    echo '[/Td]&#10';
                    echo '[/Tr]&#10';
                    echo '[/Tabela]';
                    echo '</textarea>';
                    ?>

                    <button class="copy" onclick="copy(event, this)">
                        <span>Copy all</span>
                        <img src="/app/images/copy.png" alt="copy" /></button>

                </div>

            <?php } ?>
        </div>

        <form method="GET" action="index.php">
            <?php
            if ($data["view"]) {
                echo '<input type="hidden" name="name" value="' . $data["view"]["name"] . '" />';
            }
            ?>
            <label for="location">Lokacja: </label>
            <select name="location" onchange="this.form.submit()">
                <?php
                foreach ($data["locations"] as $location) {
                    echo '<option value="' . $location . '" ';
                    $selected = $data["curr_location"] == $location ? "selected" : "";
                    $location = $location == "all" ? "-" : $location;
                    echo $selected . '>' . ucfirst($location) . '</option>';
                }
                ?>
            </select>
        </form>

        <div class="select">
            <?php foreach ($data["select"] as $select) { ?>
                <form method="get" action="index.php">
                    <input type="hidden" value="<?= $select["name"] ?>" name="name" />
                    <?php
                    if ($data["curr_location"] != "all") {
                        echo '<input type="hidden" name="location" value="' . $data["curr_location"] . '" />';
                    }
                    ?>
                    <button type="submit">
                        <img src="<?= $select["image"] ?>" alt="<?= ucfirst($select["name"]) ?>" />
                    </button>
                </form>
            <?php } ?>
            <a href="#new-entry"><span style="font-size:143px">+</span></a>
        </div>

    </main>


    <form id="new-entry" action="index.php" method="post" enctype="multipart/form-data">

        <h1>Nowa Postać</h1>

        <div class="row">
            <label for="image">Awatar:</label>
            <input type="file" name="image">
            <label for="url">lub URL:</label>
            <input type="text" name="image">
        </div>
        <div class="row">
            <label for="name">Nazwa: </label>
            <input type="text" name="name" required placeholder="George" />
            <label for="color">Dialog: </label>
            <input type="color" name="color" value="#c0c0c0" />
            <label for="location">Lokacja: </label>
            <input type="text" name="location" />
        </div>
        <label for="appearance">Opis: </label>
        <textarea name="appearance" required placeholder="Wygląd i charakter"></textarea>
        <div class="row">
            <label for="strength">S: </label>
            <input type="number" name="strength" required placeholder="1" />
            <label for="endurance">W: </label>
            <input type="number" name="endurance" required placeholder="1" />
            <label for="speed">Sz: </label>
            <input type="number" name="speed" required placeholder="1" />
            <label for="reaction">R: </label>
            <input type="number" name="reaction" required placeholder="1" />
            <label for="magic energy control">KEM: </label>
            <input type="number" name="magic energy control" placeholder="1" />
            <label for="concentration">K: </label>
            <input type="number" name="concentration" placeholder="1" />
        </div>
        <label for="magic">Magia: </label>
        <textarea name="magic" placeholder="Rodzaj magii i zaklęcia"></textarea>

        <input type="submit" value="Wyślij" />

    </form>


    <script src="index.js"></script>
</body>

</html>