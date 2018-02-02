val customCSS = """
@media screen {

    body {
        text-align: center;
        background: lightgray;
    }
    body > div,
    .aurora_export-container {
        background: white;
        max-width: 800px;
        display: inline-block;
        text-align: left;
    }
}

* {
    -webkit-print-color-adjust: exact;
    box-sizing: border-box;
}
.aurora_export-challenge-container, .aurora_export-intro {
    page-break-after: always;
}

h2 {
    font-size: 2em;
    margin-bottom: 0;
}
h3 {
    font-weight: bold;
}

.aurora_export-intro-container {
    width: 100%;
    height: 100vh;
    margin: auto;
    position: relative;
    overflow: hidden;
}
.aurora_export-intro-container > img {
    position: absolute;
    height: 100%;
}
@media print {
    .aurora_export-intro-container > img {
        height: 98%;
    }
}

.aurora_export-intro {
    margin-top: 35vh;
    position: absolute;
    padding: 2em;
    background: white;
    text-align: center;

    width: 25em;
    display: inline-block;
    left: 0;
    right: 0;
    margin-left: auto;
    margin-right: auto;

    border-radius: 15px;
}

.aurora_export-challenge > .aurora_export-general-container {
    padding: 1.8em 0 1.8em 0;
    text-align: center;
    background: #6fb53d;
}
.aurora_export-challenge > .aurora_export-general-container > .aurora_export-general {
    display: inline-block;
    width: 80%;
    background: white;
    padding: 2em;
}

.aurora_export-tasks-container {
    background: #88e04a;

}
.aurora_export-tasks {
    margin: 0 0 0 2%;
    width: 98%;
    background: white;
}

.aurora_export-task .aurora_export-title {
    padding: 1em;
    page-break-inside: avoid;
    background: #88e04a;
    width: 32%;

    /* full height */
    display: inline-block;
    height: 100%;
    position: absolute;
    top: 0;
    bottom: 0;

    margin-left: -1px; /* print view fix */
}


.aurora_export-title h3 {
     font-size: 1.3em;
}

.aurora_export-title img {
    max-width: 200px;
    margin-top: 1em;
}

.aurora_export-title-container {
    position: relative;
    overflow: hidden;
}

.aurora_export-task-description {
    background: #88e04a;
    margin-left: -2px;
    margin-bottom: 0px !important; /* override challenge.css style */
    padding: 1em 2em 1em 1em;

    display: inline-block;
    width: 70%;
    margin-left: 31%;

    min-height: 22em;
}

.aurora_export-revision {
    padding: 1em 2em 1em 1em;
}

.aurora_export-task-content {
    padding-left: 2px;
}

.aurora_export-task-images {
    padding: 1em;
}

.aurora_export-task-images::after {
    content: " ";
    display: block;
    clear: both;
}

.aurora_export-task-images:empty {
    display: none;
}

.aurora_export-task-image-box {
    padding: 0.5em;
    float: left;
}
.aurora_export-task-image-box span {
    font-weight: bold;
}

.aurora_export-task-image-box img.print {
    display: none;
}

@media print {
    .aurora_export-task-image-box:not(.pdf) img.non-print {
        display: none;
    }
    .aurora_export-task-image-box:not(.pdf) {
        width: 50%;
    }
    .aurora_export-task-image-box:not(.pdf) img.print {
        display: inline;
        width: 100%;
    }


}

        """