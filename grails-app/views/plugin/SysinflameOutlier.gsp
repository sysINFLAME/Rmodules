<!--  
 @Author Benjamin Baum, Department of Medical Informatics, Göttingen, <benjamin.baum@med.uni-goettingen.de> 
-->
%{--include js lib for heatmap dynamically--}%
<r:require modules="sysinflameOutlier"/>
<r:layoutResources disposition="defer"/>

<div id="analysisWidget">
    <h2>
        Variable Selection
        <a href='JavaScript:D2H_ShowHelp(1290,helpURL,"wndExternal",CTXT_DISPLAY_FULLHELP )'>
            <img src="${resource(dir: 'images', file: 'help/helpicon_white.jpg')}" alt="Help"/>
        </a>
    </h2>


    <form id="analysisForm">
        <fieldset class="inputFields">
            %{--High dimensional input--}%
            <div class="highDimContainer">
                <span>Drag <b>numerical</b> concepts from the tree into the box below that you wish to generate outlier statistics on.</span>
                <div id='divVariables' class="queryGroupIncludeSmall highDimBox"></div>
                <div class="highDimBtns">
                    <button type="button" onclick="sysinflameOutlierView.clear_high_dimensional_input('divVariables')">Clear</button>
                </div>
            </div>
         </fieldset>

        <fieldset class="toolFields">
            <input type="button" value="Run" onClick="sysinflameOutlierView.submit_job(this.form);" class="runAnalysisBtn">
        </fieldset>
    </form>

</div>



