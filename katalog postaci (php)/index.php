<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    echo "POST detected: ";
    print_r($_POST);
    header("Location: ".$_SERVER['PHP_SELF']."?"."name=2");
    exit;
}
print_r($_GET);
?>
<form method="POST" action=".">
    <input type="text" value="1" name="p">
    <input type="submit" value="Submit">
</form>

<form method="GET" action=".">
    <input type="text" value="2" name="g">
    <input type="submit" value="Submit">
</form>



<!-- FOOTER -->
<style>
    @keyframes left_to_right {
        from {
            left: -20%;
        }

        to {
            left: 100%;
        }
    }

    footer {
        position: fixed;
        bottom: 0;
        left: 0;
        width:100%;
        height: 22px;
        background-color: #ccc;
    }
    footer > p{
        position: fixed;
        bottom: 0;
        animation: left_to_right 30s linear infinite;
        width:100%;
        margin: 0;
    }
</style>
<footer><p>TO JEST STRONA TESTOWA</p></footer>