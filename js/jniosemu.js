var images = [new Image(), new Image()];
images[0].src = 'gfx/runbutton.png';
images[1].src = 'gfx/runbutton_on.png';

function mouseover() {
    document.getElementById('runbutton').src = images[1].src;
}

function mouseout() {
    document.getElementById('runbutton').src = images[0].src;
}
