// ########################## RDF Browser Window ##########################
function showBrowser(centralNode, centralNodeURI) {
	$('#browser').show(ANIMATION_SPEED);
	$('#entrypoint').hide(ANIMATION_SPEED);

	prepareBrowser(centralNode, centralNodeURI);
}

function prepareBrowser(centralNode, centralNodeURI) {
	var xhttp = new XMLHttpRequest();

	showLoader(centralNode);
	updateBrowsingHistory(centralNode, centralNodeURI);

	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			updateBrowserHeight();
			displayNodes(centralNode, centralNodeURI, JSON.parse(xhttp.responseText));
		}
	}

	xhttp.open('GET', getNeighborhoodRequest(centralNodeURI), true);
	xhttp.send();
}

function displayNodes(centralNode, centralNodeURI, neighbors) {
	// Clear the container.
	$('#browserBody').html('<div id="container" data-central-node="' + centralNode + '" data-central-node-uri="' + centralNodeURI + '"></div>');

	// Determine how to display the graph.
	enableExport(true);

	switch (getBrowsingType()) {
		case 'circular':
			arrangeNodesCircular(centralNode, centralNodeURI, neighbors);
			break;
		case 'direction':
			arrangeNodesByDirection(centralNode, centralNodeURI, neighbors);
			break;
		case 'random':
			arrangeNodesRandomized(centralNode, centralNodeURI, neighbors);
			break;
		case 'textual':
			enableExport(false);
			displayNodesTextual(centralNode, centralNodeURI, neighbors);
			break;
		default:
			console.error('Undefined browsing type.');
			break;
	}
}

function enableExport(enable) {
	$('#btnExportDropdown').prop('disabled', !enable);
}

function showLoader(centralNode) {
	$('#browserBody').html('<p>Computing the neighbors for ' + centralNode + ' ...</p>' + LOADER);
}

function updateBrowserHeight() {
	// Update the height of the body div w.r.t. to the outer divs.
	var headerTop = $('#browserHeader').offset().top;
	var headerHeight = $('#browserHeader').outerHeight();
	var bottomSpace = 40;

	// For a fullscreen browser, we only have to respect the header height.
	var heightDiff = $('#browser').hasClass('fullscreen')
		? headerHeight
		: headerTop + headerHeight + bottomSpace;

	$('#browserBody').css('height', 'calc(100vh - ' + heightDiff + 'px)');
}

function reloadGraph() {
	// Reload the graph with the stored values for the central node.
	var centralNode = $('#container').attr('data-central-node');
	var centralNodeURI = $('#container').attr('data-central-node-uri');

	removeLastHistoryElement();
	prepareBrowser(centralNode, centralNodeURI);
}

function toggleBrowserFullscreen() {
	$('#browser').toggleClass('fullscreen');
	updateBrowserHeight();
}

function closeBrowser() {
	$('#browser').hide(ANIMATION_SPEED);
	$('#entrypoint').show(ANIMATION_SPEED);
	showReturnToBrowser();
}

function returnToBrowser() {
	$('#browser').show(ANIMATION_SPEED);
	$('#entrypoint').hide(ANIMATION_SPEED);
}

$(document).ready(function() {
	$('#btnReloadGraph').click( function() {
		reloadGraph();
	});
	$('#btnExportGraphPNG').click( function() {
		exportGraphAsPNG();
	});
	$('#btnExportGraphSVG').click( function() {
		exportGraphAsSVG();
	});
	$('#btnFullscreenBrowser').click( function() {
		toggleBrowserFullscreen();
	});
	$('#btnCloseBrowser').click( function() {
		closeBrowser();
	});
});
