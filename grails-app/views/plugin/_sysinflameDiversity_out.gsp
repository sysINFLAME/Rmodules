<h2>Alpha-Diversity</h2>

<p>
    <div class="plot_hint">
        Click on the image to open it in a new window as this may increase readability.
        <br><br>
    </div>

    <g:each var="location" in="${imageLocations}">
        <a onclick="window.open('${resource(file: location, dir: "images")}', '_blank')">
            <g:img style="height=10%" file="${location}" class="img-result-size_sysinflame"></g:img>
        </a>
    </g:each>
    <div>
        <a href="${resource(file: zipLink)}" class="downloadLink">Download raw R data</a>
    </div>
</p>

