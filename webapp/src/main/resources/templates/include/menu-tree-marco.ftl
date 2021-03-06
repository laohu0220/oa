<#macro menuTreeLoop mtlist>
    <#list mtlist as mt>

    <li class="<#if mt.spread>open</#if>">
        <a href="#" class="dropdown-toggle">
            <div class="menu-name">
                <span class="glyphicon glyphicon-folder-open"></span>
                <span class="menu-text">${mt.menu.menuName!""}</span>
            </div>
        </a>
        <ul class="submenu" <#if mt.spread>style="display: block"</#if>>
            <#if (mt.menuTreeList?size>0)>
                <@menuTreeLoop mt.menuTreeList></@menuTreeLoop>
            </#if>
            <#list mt.menu.functionSet as function>
                <li class="open">
                    <a href="${ctx}${function.functionUrl}" class="dropdown-toggle">
                        <div class="menu-name">
                            <i class="icon-file-alt"></i>
                            <span class="glyphicon glyphicon-link"></span>
                            <span class="menu-text">${function.functionName!""}</span>
                            <b class="arrow icon-angle-down"></b>
                        </div>
                    </a>
                </li>
            </#list>
        </ul>

    </li>


    </#list>
</#macro>