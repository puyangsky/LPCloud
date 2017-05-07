/*
code:    editable.js  
version: v1.0
date:    2011/10/21
author:  lyroge@foxmail.com
usage:
$("table").editable({   selector 可以选择table或者tr
head: true,         是否有标题
noeditcol: [1, 0],  哪些列不能编辑

编辑列配置：colindex：列索引
edittype：编辑时显示的元素 0：input 1：checkbox 2：select
ctrid：关联元素的id  如edittype=2， 那么需要设置select的元素
css:元素的样式
editcol: [{ colindex: 2, edittype: 2, ctrid: "sel",css:""}],
onok: function () {                                              
return true;    根据结果返回true or false                     
},
ondel: function () {
return true;    根据结果返回true or false
}
});
*/

(function ($) {
    $.fn.editable = function (options) {
        options = options || {};
        opt = $.extend({}, $.fn.editable.defaults, options);

        trs = [];
        $.each(this, function () {
            if (this.tagName.toString().toLowerCase() == "table") {
                $(this).find("tr").each(function () {
                    trs.push(this);
                });
            }
            else if (this.tagName.toString().toLowerCase() == "tr") {
                trs.push(this);
            }
        });

        $trs = $(trs);
        if ($trs.size() == 0 || (opt.head && $trs.size() == 1))
            return false;

        var button = "<td><a href='#' class='" + opt.editcss + "'>编辑</a> <a href='#' class='" + opt.delcss + "'>删除</a><a href='#' class='" + opt.onokcss + "'>确定</a> <a href='#' class='" + opt.canclcss + "'>取消</a></td>";

        $trs.each(function (i, tr) {
            if (opt.head && i == 0) {
                $(tr).append("<td></td>");
                return true;
            }
            $(tr).append(button);
        });

        $trs.find(".onok, .cancl").hide();
        $trs.find(".edit").click(function () {
            $tr = $(this).closest("tr");
            $tds = $tr.find("td");
            $.each($tds.filter(":lt(" + ($tds.size() - 1) + ")"), function (i, td) {
                if ($.inArray(i, opt.noeditcol) != -1)
                    return true;
                var t = $.trim($(td).text());
                if (opt.editcol != undefined) {
                    $.each(opt.editcol, function (j, obj) {
                        if (obj.colindex == i) {
                            css = obj.css ? "class='" + obj.css + "'" : "";
                            if (obj.edittype == undefined || obj.edittype == 0) {
                                $(td).data("v", t);
                                $(td).html("<input type='text' value='" + t + "' " + css + " />");
                            }
                            else if (obj.edittype == 2) {    //select
                                if (obj.ctrid == undefined) {
                                    alert('请指定select元素id ctrid');
                                    return;
                                }
                                $(td).empty().append($("#" + obj.ctrid).clone().show());
                                $(td).find("option").filter(":contains('" + t + "')").attr("selected", true);
                            }
                            /* 可以在此处扩展input、select以外的元素编辑行为 */
                        }
                    });
                }
                else {
                    $(td).data("v", t);
                    $(td).html("<input type='text' value='" + t + "' />");
                }
            });
            $tr.find(".onok, .cancl, .edit, .del").toggle();
            return false;
        }); ;

        $trs.find(".del").click(function () {
            $tr = $(this).closest("tr");
            if (opt.ondel()) {
                $tr.remove();
            }
            return false;
        });

        $trs.find(".onok").click(function () {
            $tr = $(this).closest("tr");
            $tds = $tr.find("td");
            if (opt.onok()) {
                $.each($tds.filter(":lt(" + ($tds.size() - 1) + ")"), function (i, td) {
                    var c = $(td).children().get(0);
                    if (c != null)
                        if (c.tagName.toLowerCase() == "select") {
                            $(td).html(c.options[c.selectedIndex].text);
                        }
                        else if (c.tagName.toLowerCase() == "input") {
                            $(td).html(c.value);
                        }
                    /* 可以在此处扩展input、select以外的元素确认行为 */
                });
                $tr.find(".onok, .cancl, .edit, .del").toggle();
            }
            return false;
        });

        $trs.find(".cancl").click(function () {
            $tr = $(this).closest("tr");
            $tds = $tr.find("td");
            $.each($tds.filter(":lt(" + ($tds.size() - 1) + ")"), function (i, td) {
                var c = $(td).children().get(0);
                if (c != null)
                    if (c.tagName.toLowerCase() == "select") {
                        $(td).html(c.options[c.selectedIndex].text);
                    }
                    else if (c.tagName.toLowerCase() == "input") {
                        $(td).html(c.value);
                    }
                /* 可以在此处扩展input、select以外的元素取消行为 */
            });
            $tr.find(".onok, .cancl, .edit, .del").toggle();
            return false;
        });
    };

    $.fn.editable.defaults = {
        head: false,
        /* 
        如果为空那么所有的列都可以编辑，并且默认为文本框的方式编辑 
        如下形式：
        {{colindex:'', edittype:'', ctrid:'', css:''}, ...}
        edittype 0:input 1:checkbox 2:select
        */
        //editcol:{},    
        /* 
        设置不可以编辑的列，默认为空 
        如下形式：
        [0,2,3,...]
        */
        noeditcol: [],
        onok: function () {
            alert("this's default onok click event");
            return true;
        },
        ondel: function () {
            alert("this's default on del click event");
            return true;
        },
        editcss: "edit",
        delcss: "del",
        onokcss: "onok",
        canclcss: "cancl"
    };
})(jQuery);