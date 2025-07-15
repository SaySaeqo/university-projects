function copy(e, el){

    var text = el.parentElement.innerHTML;
    
    let special_phrase = '<textarea class="code" readonly="">';
    let special_phrase_end = '</textarea>';
    if(text.includes(special_phrase)){
        text = text.slice(text.indexOf(special_phrase) + special_phrase.length, text.indexOf(special_phrase_end));
    } else{
        text = text.replace(/<button class="copy".{1,100}<\/button>/g, "");
        text = text.replace(/<font color="#(\w+)">/g, '[color="#$1"]');
        text = text.replace(/&nbsp;/g, ' ');
        text = text.replace(/<(\/?)b>/g, "[$1b]");
        text = text.replace(/<\/font>/g, "[/color]");
        text = text.replace(/^\s*/g, "");
        text = text.replace(/\s*$/g, "");
    }
    
    navigator.clipboard.writeText(text);
}