%{--include js lib for heatmap dynamically--}%
<r:require modules="phenomap"/>
<r:layoutResources disposition="defer"/>

<div id="analysisWidget">

    <h2>
        Variable Selection
        <a href='JavaScript:D2H_ShowHelp(1505,helpURL,"wndExternal",CTXT_DISPLAY_FULLHELP )'>
            <img src="${resource(dir: 'images', file: 'help/helpicon_white.jpg')}" alt="Help"/>
        </a>
    </h2>

                       

<form id="analysisForm">
        <fieldset class="inputFields">
            %{--High dimensional input--}%
            <div class="highDimContainer">
                <span>Drag two or more concepts from the tree into the box below that you wish to generate a heatmap on.</span>
                




 <div id='divCategoryVariable' class="queryGroupIncludeLong divInputBox"></div>

                <div class="highDimBtns">
                    <button type="button" onclick="phenoMapView.clear_high_dimensional_input('divCategoryVariable')">Clear</button>
                </div>
</div>


         </fieldset>

        <fieldset class="toolFields">
            <input type="button" value="Run" onClick="phenoMapView.submit_job(this.form);" class="runAnalysisBtn">
        </fieldset>
    </form>

</div>

