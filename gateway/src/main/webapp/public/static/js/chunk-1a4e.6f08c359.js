(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-1a4e"],{"/SD7":function(e,t,i){"use strict";var a=i("hxdy");i.n(a).a},"3b06":function(e,t,i){},"5OJy":function(e,t,i){"use strict";var a=i("VIpa");i.n(a).a},"6yrG":function(e,t,i){},"8sHl":function(e,t,i){"use strict";var a=i("6yrG");i.n(a).a},"9GgJ":function(e,t,i){"use strict";var a={name:"XrHeader",components:{},props:{iconClass:[String,Array],iconColor:String,label:String,showSearch:{type:Boolean,default:!1},searchIconType:{type:String,default:"text"},placeholder:{type:String,default:"请输入内容"},ftTop:{type:String,default:"15px"},content:[String,Number],inputAttr:{type:Object,default:function(){}}},data:function(){return{search:""}},computed:{},watch:{content:{handler:function(){this.search!=this.content&&(this.search=this.content)},immediate:!0}},mounted:function(){},beforeDestroy:function(){},methods:{inputChange:function(){this.$emit("update:content",this.search)},searchClick:function(){this.$emit("search",this.search)}}},l=(i("zIzm"),i("KHd+")),n=Object(l.a)(a,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("flexbox",{staticClass:"xr-header"},[e.iconClass?i("div",{staticClass:"xr-header__icon",style:{backgroundColor:e.iconColor}},[i("i",{class:e.iconClass})]):e._e(),e._v(" "),i("div",{staticClass:"xr-header__label"},[e.$slots.label?e._t("label"):[e._v(e._s(e.label))]],2),e._v(" "),e.showSearch?i("el-input",e._b({staticClass:"xr-header__search",class:{"is-text":"text"===e.searchIconType},style:{"margin-top":e.ftTop},attrs:{placeholder:e.placeholder},on:{input:e.inputChange},nativeOn:{keyup:function(t){return"button"in t||!e._k(t.keyCode,"enter",13,t.key,"Enter")?e.searchClick(t):null}},model:{value:e.search,callback:function(t){e.search=t},expression:"search"}},"el-input",e.inputAttr,!1),["text"===e.searchIconType?i("el-button",{attrs:{slot:"append",type:"primary"},nativeOn:{click:function(t){return e.searchClick(t)}},slot:"append"},[e._v("搜索")]):i("el-button",{attrs:{slot:"append",icon:"el-icon-search"},nativeOn:{click:function(t){return e.searchClick(t)}},slot:"append"})],1):e._e(),e._v(" "),i("div",{staticClass:"xr-header__ft",style:{top:e.ftTop}},[e._t("ft")],2)],1)},[],!1,null,"7bba770c",null);n.options.__file="index.vue";t.a=n.exports},DNY4:function(e,t,i){},Ed9r:function(e,t,i){},Pqxw:function(e,t,i){e.exports={xrColorPrimary:"#2362FB"}},Tm3p:function(e,t,i){"use strict";var a=i("vpoJ");i.n(a).a},UGe0:function(e,t,i){"use strict";var a=i("XAon"),l=i("3pgX"),n=i("Woz+"),o=i("8GhS"),s=i("DMJz"),r=i("tG22"),d=i("bWDp"),c={name:"WkForm",components:{WkUserSelect:a.a,WkDepSelect:l.a,VDistpicker:n.a,XhFiles:o.d,WkSelect:s.a,WkCheckbox:r.a},mixins:[d.a],inheritAttrs:!1,props:{fieldFrom:{type:Object,default:function(){return{}}},fieldList:{type:Array,default:function(){return[]}},customClass:{type:String,default:"is-two-columns"}},data:function(){return{instance:null}},computed:{},watch:{},mounted:function(){var e=this;this.$nextTick(function(){e.instance=e.$refs.form})},beforeDestroy:function(){},methods:{}},u=(i("mYM5"),i("X+Pv"),i("KHd+")),f=Object(u.a)(c,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("el-form",e._g(e._b({ref:"form",staticClass:"wk-form",class:e.customClass},"el-form",e.$attrs,!1),e.$listeners),[e._l(e.fieldList,function(t,a){return i("el-form-item",{key:a,class:[t.className||"","is-"+t.formType],attrs:{prop:t.field}},[i("template",{slot:"label"},[e._v("\n      "+e._s(t.name)+"\n      "),"tooltip"==t.tipType?i("el-tooltip",{attrs:{effect:"dark",placement:"top"}},[i("div",{attrs:{slot:"content"},domProps:{innerHTML:e._s(e.getTips(t))},slot:"content"}),e._v(" "),i("i",{staticClass:"wk wk-help wk-help-tips"})]):i("span",{staticStyle:{color:"#999"}},[e._v("\n        "+e._s(e.getTips(t))+"\n      ")])],1),e._v(" "),"text"==t.formType?i("el-input",{attrs:{disabled:t.disabled,maxlength:100,placeholder:t.placeholder,type:t.formType},on:{input:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):e.isTrimInput(t.formType)?i("el-input",{attrs:{disabled:t.disabled,"prefix-icon":e.getInputIcon(t.formType),maxlength:e.getInputMaxlength(t.formType),placeholder:t.placeholder,type:"text"},on:{input:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,"string"==typeof i?i.trim():i)},expression:"fieldFrom[item.field]"}}):"number"==t.formType?i("el-input-number",{attrs:{placeholder:t.placeholder,disabled:t.disabled,controls:!1},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"floatnumber"==t.formType?i("el-input-number",{attrs:{placeholder:t.placeholder,disabled:t.disabled,controls:!1},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"textarea"==t.formType?i("el-input",{attrs:{disabled:t.disabled,rows:3,autosize:{minRows:3},maxlength:t.maxlength||800,placeholder:t.placeholder,type:t.formType,resize:"none"},on:{input:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"select"===t.formType&&t.hasOwnProperty("optionsData")?i("wk-select",{attrs:{disabled:t.disabled,clearable:t.clearable,placeholder:t.placeholder,options:t.setting,"show-type":1===t.precisions?"tiled":"default","other-show-input":t.hasOwnProperty("optionsData")},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"checkbox"===t.formType&&t.hasOwnProperty("optionsData")?i("wk-checkbox",{attrs:{disabled:t.disabled,clearable:t.clearable,placeholder:t.placeholder,options:t.setting,"show-type":1===t.precisions?"tiled":"default","other-show-input":t.hasOwnProperty("optionsData")},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):["checkbox","select"].includes(t.formType)?i("el-select",{staticStyle:{width:"100%"},attrs:{disabled:t.disabled,clearable:t.clearable,placeholder:t.placeholder,multiple:"checkbox"===t.formType},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}},e._l(t.setting,function(t,a){return i("el-option",{key:a,attrs:{label:e.isEmptyValue(t.value)?t:t.label||t.name,value:e.isEmptyValue(t.value)?t:t.value}})})):"checkbox"==t.formType?i("el-select",{staticStyle:{width:"100%"},attrs:{disabled:t.disabled,clearable:t.clearable,placeholder:t.placeholder,multiple:""},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}},e._l(t.setting,function(t,a){return i("el-option",{key:a,attrs:{label:e.isEmptyValue(t.value)?t:t.label||t.name,value:e.isEmptyValue(t.value)?t:t.value}})})):"date"==t.formType?i("el-date-picker",{staticStyle:{width:"100%"},attrs:{disabled:t.disabled,clearable:"",type:"date","value-format":"yyyy-MM-dd",placeholder:"选择日期"},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"dateRange"==t.formType?i("el-date-picker",{staticStyle:{width:"100%"},attrs:{disabled:t.disabled,type:t.dateType||"daterange","value-format":t.dateValueFormat||"yyyy-MM-dd",clearable:"","start-placeholder":"开始日期","end-placeholder":"结束日期"},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"datetime"==t.formType?i("el-date-picker",{staticStyle:{width:"100%"},attrs:{disabled:t.disabled,clearable:"",type:"datetime","value-format":"yyyy-MM-dd HH:mm:ss",placeholder:"选择日期"},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"structure"==t.formType?i("wk-dep-select",{staticStyle:{width:"100%"},attrs:{request:t.request,props:t.props,params:t.params,disabled:t.disabled,radio:!!e.isEmptyValue(t.radio)||t.radio},on:{change:function(i){e.depOrUserChange(t,a,arguments[0],arguments[1])}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):["single_user","user"].includes(t.formType)?i("wk-user-select",{staticStyle:{width:"100%"},attrs:{request:t.request,props:t.props,params:t.params,disabled:t.disabled,radio:!!e.isEmptyValue(t.radio)||t.radio},on:{change:function(i){e.depOrUserChange(t,a,arguments[0],arguments[1])}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"radio"==t.formType?i("el-radio-group",{attrs:{disabled:t.disabled,placeholder:t.placeholder},on:{change:function(i){e.commonChange(t,a,i)}},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}},e._l(t.setting,function(t,a){return i("el-radio",{key:a,attrs:{label:e.isEmptyValue(t.value)?t:t.value}},[e._v("\n        "+e._s(e.isEmptyValue(t.value)?t:t.label||t.name)+"\n      ")])})):"boolean_value"==t.formType?i("el-switch",{attrs:{disabled:t.disabled,"active-value":"1","inactive-value":"0"},model:{value:e.fieldFrom[t.field],callback:function(i){e.$set(e.fieldFrom,t.field,i)},expression:"fieldFrom[item.field]"}}):"address"==t.formType?i("v-distpicker",{attrs:{province:e.fieldFrom[t.field].province,city:e.fieldFrom[t.field].city,area:e.fieldFrom[t.field].area},on:{province:function(i){e.selectProvince(i,t,a)},city:function(i){e.selectCity(i,t,a)},area:function(i){e.selectArea(i,t,a)}}}):"file"==t.formType?i("xh-files",{attrs:{value:e.fieldFrom[t.field],disabled:t.disabled},on:{"value-change":function(i){e.oldChange(i,t,a)}}}):[e._t("default",null,{data:t,index:a})]],2)}),e._v(" "),e._t("suffix")],2)},[],!1,null,"61367085",null);f.options.__file="index.vue";t.a=f.exports},VIpa:function(e,t,i){},"X+Pv":function(e,t,i){"use strict";var a=i("Ed9r");i.n(a).a},eGUX:function(e,t,i){"use strict";var a=i("3b06");i.n(a).a},g3Fd:function(e,t,i){},hxdy:function(e,t,i){},iWcH:function(e,t,i){"use strict";i.r(t);var a=i("QbLZ"),l=i.n(a),n=i("W3mQ"),o=i("rrw3"),s=i("Vm8M"),r=i("u2hk"),d={name:"WkDetailTableView",components:{WkFieldView:function(){return Promise.resolve().then(i.bind(null,"iWcH"))}},props:{title:String,showType:{type:String,default:"defalut"},addFieldList:Array,fieldForm:{type:Array,default:function(){return[]}},fieldList:{type:Array,default:function(){return[]}}},data:function(){return{}},computed:{},watch:{},created:function(){},mounted:function(){},beforeDestroy:function(){},methods:{getMinWidth:function(e){return"date_interval"===e||"dateRange"===e||"file"===e||"location"===e||"position"===e?250:150},getShowValue:function(e){return!e.hasOwnProperty("show")||e.show}}},c=(i("rs0F"),i("KHd+")),u=Object(c.a)(d,function(){var e=this,t=e.$createElement,i=e._self._c||t;return e.fieldForm&&e.fieldForm.length>0?i("div",{staticClass:"wk-detail-table-view"},["default"===e.showType?e._l(e.fieldList,function(t,a){return i("div",{key:a,staticClass:"detail-item"},[i("flexbox",{staticClass:"detail-item__head"},[i("div",{staticClass:"detail-item__head-title"},[e._v(e._s(e.title)+"（"+e._s(a+1)+"）")])]),e._v(" "),i("flexbox",{staticClass:"wk-form-items",attrs:{align:"flex-start",wrap:"wrap",justify:"flex-start"}},[e._l(t,function(t,l){return[e.getShowValue(t)?i("div",{key:l,staticClass:"wk-form-item",class:["is-"+t.formType],style:{width:t.stylePercent?t.stylePercent+"%":"auto"},attrs:{label:t.name}},[i("div",{staticClass:"wk-form-item__label"},[e._v(e._s(t.name))]),e._v(" "),i("wk-field-view",{attrs:{props:t,"form-type":t.formType,value:e.fieldForm[a][t.field]},scopedSlots:e._u([{key:"default",fn:function(t){var i=t.data;return[e._t("default",null,{data:i})]}}])})],1):e._e()]})],2)],1)}):"table"===e.showType?i("div",{staticClass:"detail-item"},[i("el-table",{staticClass:"wk-table-items",staticStyle:{width:"100%"},attrs:{data:e.fieldForm,"row-key":Date.now().toString()}},e._l(e.addFieldList,function(t,a){return e.getShowValue(t)?i("el-table-column",{key:a,attrs:{prop:t.field,label:t.name,"min-width":e.getMinWidth(t.formType)},scopedSlots:e._u([{key:"default",fn:function(a){var l=a.row;return a.column,a.$index,[i("wk-field-view",{attrs:{props:t,"form-type":t.formType,value:l[t.field]},scopedSlots:e._u([{key:"default",fn:function(t){var i=t.data;return[e._t("default",null,{data:i})]}}])})]}}])}):e._e()}))],1):e._e()],2):e._e()},[],!1,null,"42bedbf9",null);u.options.__file="View.vue";var f=u.exports,m=i("jtZb"),p=i("a/5N"),h=i("m77o"),v={signatureHeight:"26px"},b={name:"WkFieldView",components:{WkSignatureImage:n.a,WkDescText:o.a,MapView:s.a,FileListView:r.a,WkDetailTableView:f},props:{props:Object,formType:String,value:[String,Object,Array,Number],ignoreFields:{type:Array,default:function(){return[]}}},data:function(){return{mapViewShow:!1}},computed:{config:function(){return Object(m.a)(l()({},v),this.props||{})},isEmpty:function(){return Object(p.b)(this.value)},isCommonType:function(){return["text","textarea","website","select","checkbox","number","floatnumber","percent","mobile","email","date","datetime","date_interval","user","structure","position"].includes(this.formType)}},watch:{},created:function(){},mounted:function(){},beforeDestroy:function(){},methods:{objectHasValue:function(e,t){return!!Object(p.c)(e)&&!Object(p.b)(e[t])},openUrl:function(e){e.match(/^https?:\/\//i)||(e="http://"+e),window.open(e)},getCommonShowValue:function(){return Object(h.a)(this.formType,this.value,"",this.props)}}},y=(i("Tm3p"),Object(c.a)(b,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"wk-field-view",class:"is-"+e.formType},[e.ignoreFields.includes(e.props.field)?[e._t("default",null,{data:e.$props})]:e.isCommonType?i("span",[e._v(e._s(e.getCommonShowValue()))]):"boolean_value"==e.formType?i("el-switch",{attrs:{value:e.value,disabled:"","active-value":"1","inactive-value":"0"}}):"handwriting_sign"==e.formType?i("wk-signature-image",{attrs:{src:e.value,height:e.config.signatureHeight}}):"desc_text"==e.formType?i("wk-desc-text",{key:Date.now().toString(),attrs:{value:e.value}}):"location"==e.formType?i("span",{class:{"can-check":e.objectHasValue(e.value,"address")},on:{click:function(t){t.stopPropagation(),e.mapViewShow=!0}}},[e._v(e._s(e.objectHasValue(e.value,"address")?e.value.address:""))]):"website"==e.formType?i("span",{class:{"can-check":!e.isEmpty},on:{click:function(t){t.stopPropagation(),e.openUrl(e.value)}}},[e._v(e._s(e.value))]):"file"==e.formType?i("file-list-view",{attrs:{list:e.value||[]}}):"detail_table"==e.formType?i("wk-detail-table-view",{attrs:{"show-type":2===e.props.precisions?"table":"default",title:e.props.name,"add-field-list":e.props.fieldExtendList,"field-form":e.value,"field-list":e.props.fieldList},scopedSlots:e._u([{key:"default",fn:function(t){var i=t.data;return[e._t("default",null,{data:i})]}}])}):[e._t("default",null,{data:e.$props})],e._v(" "),e.mapViewShow?i("map-view",{attrs:{title:e.value.address,lat:e.value.lat,lng:e.value.lng},on:{hidden:function(t){e.mapViewShow=!1}}}):e._e()],2)},[],!1,null,"25557cb0",null));y.options.__file="WkFieldView.vue";t.default=y.exports},ihDC:function(e,t,i){},jzeO:function(e,t,i){"use strict";var a={name:"Reminder",components:{},props:{closeShow:{type:Boolean,default:!1},content:{type:String,default:"内容"},fontSize:{type:String,default:"13"}},data:function(){return{}},computed:{},mounted:function(){},destroyed:function(){},methods:{close:function(){this.$emit("close")}}},l=(i("/SD7"),i("KHd+")),n=Object(l.a)(a,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("flexbox",{staticClass:"reminder-wrapper"},[i("flexbox",{staticClass:"reminder-body",attrs:{align:"stretch"}},[i("i",{staticClass:"wk wk-warning reminder-icon"}),e._v(" "),i("div",{staticClass:"reminder-content",style:{"font-size":e.fontSize+"px"},domProps:{innerHTML:e._s(e.content)}}),e._v(" "),e._t("default"),e._v(" "),e.closeShow?i("i",{staticClass:"el-icon-close close",on:{click:e.close}}):e._e()],2)],1)},[],!1,null,"d9a726d6",null);n.options.__file="Reminder.vue";t.a=n.exports},m77o:function(e,t,i){"use strict";i.d(t,"a",function(){return s});var a=i("a/5N"),l=i("dAOq"),n=i("IEYw"),o=i("7Qib");function s(e,t){var i=arguments.length>2&&void 0!==arguments[2]?arguments[2]:"--";arguments[3];if("position"===e)return Object(a.a)(t)?t.map(function(e){return e.name}).join():i;if("date"===e)return Object(o.u)(t);if("location"===e)return Object(a.c)(t)?t.address:i;if("date_interval"===e)return Object(a.a)(t)?t.join("-"):i;if("percent"===e)return Object(a.b)(t)?i:t+"%";if("single_user"===e)return Object(a.c)(t)?t.realname||i:t||i;if("select"===e){var s=n.a.methods.getRealParams({formType:e},t);return Object(a.b)(s)?i:s}if("checkbox"===e){var r=n.a.methods.getRealParams({formType:e},t);return Object(a.b)(r)?i:r}return"structure"===e?Object(a.a)(t)?t.map(function(e){return e.name||e.deptName}).join()||i:t||i:"user"===e?Object(a.a)(t)?t.map(function(e){return e.realname||e.employeeName}).join()||i:t||i:"check_status"===e?l.a.methods.getStatusName(t):Object(a.b)(t)?i:t}},mYM5:function(e,t,i){"use strict";var a=i("Pqxw");i.n(a).a},nboU:function(e,t,i){"use strict";t.a={watch:{loading:function(e){if(e){var t=this.$refs.wkDialog.$refs.dialog;this.loadingInstance=this.$loading({target:t})}else this.loadingInstance&&this.loadingInstance.close()}}}},rs0F:function(e,t,i){"use strict";var a=i("g3Fd");i.n(a).a},u2hk:function(e,t,i){"use strict";var a=i("7Qib"),l={name:"FileListView",filters:{fontSizeValue:function(e){return Object(a.h)(e)}},props:{list:{type:Array,required:!0}},data:function(){return{}},methods:{getFileTypeIcon:function(e){if(!e)return"";var t=e?e.split("."):[],i="";return i=t.length>0?t[t.length-1]:"",Object(a.n)(i)},downloadClick:function(e){Object(a.d)({path:e.filePath||e.url,name:e.name})},previewClick:function(e,t){this.$wkPreviewFile.preview({index:t,data:this.list.map(function(e){return{url:e.filePath||e.url,name:e.name}})})}}},n=(i("8sHl"),i("KHd+")),o=Object(n.a)(l,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"file-list-view"},e._l(e.list,function(t,a){return i("div",{key:a,staticClass:"file-item"},[i("img",{staticClass:"pic-icon",attrs:{src:e.getFileTypeIcon(t.name),alt:""}}),e._v(" "),i("div",{staticClass:"file-name text-one-line"},[e._v(e._s(t.name))]),e._v(" "),i("div",{staticClass:"file-size"},[e._v("( "+e._s(e._f("fontSizeValue")(t.size))+" )")]),e._v(" "),i("div",{staticClass:"down"},[i("span",{on:{click:function(i){e.previewClick(t,a)}}},[e._v("预览")]),e._v(" "),i("span",{on:{click:function(i){e.downloadClick(t)}}},[e._v("下载")])])])}))},[],!1,null,"31b6e124",null);o.options.__file="FileListView.vue";t.a=o.exports},v3P6:function(e,t,i){"use strict";var a=i("DNY4");i.n(a).a},vc3k:function(e,t,i){"use strict";i.r(t);var a=i("9GgJ"),l=i("t3Un");var n=i("j3/X"),o=i("0WXr"),s=i("JPSa"),r=i("7Qib"),d=i("a/5N"),c=i("ixM/"),u=i("IEYw"),f={name:"EmployeePostInfo",components:{WkBaseDetail:n.a,WkCustomBaseDetail:o.a},mixins:[u.a],props:{},data:function(){return{loading:!1,leaveDetail:null,postEditList:[],postList:[],leaveFields:[{label:"计划离职日期",field:"planQuitTime",value:""},{label:"申请离职日期",field:"applyQuitTime",value:""},{label:"薪资结算日期",field:"salarySettlementTime",value:""},{label:"离职类型",field:"quitType",value:""},{label:"离职原因",field:"quitReason",value:""},{label:"备注",field:"remarks",value:""}],leaveList:[]}},computed:{},watch:{},mounted:function(){this.getDetail()},methods:{getDetail:function(){var e=this;this.loading=!0,Object(l.a)({url:"hrmEmployeeArchives/postArchives",method:"post"}).then(function(t){e.loading=!1;var i=t.data.information||[];e.postEditList=Object(r.w)(i);var a=[];i.forEach(function(e){e.value=e.fieldValue,"employment_forms"==e.fieldName?e.value=c.d.employmentFormsValue[e.value]:"probation"==e.fieldName?e.value=c.d.probationValue[e.value]:"status"==e.fieldName?e.value=c.d.statusValue[e.value]:"user"==e.formType||"structure"==e.formType||"recruit_channel"==e.formType||"area"==e.formType?e.value=e.fieldValueDesc:"company_age"==e.fieldName&&(e.value=e.fieldValueDesc),a.push(e)});var l=e.getFormAssistIds([a]),n=[];a.forEach(function(t){if(e.getFormItemDefaultProperty(t,!1),t.show=!l.includes(t.formAssistId),"detail_table"===t.formType){if(!Object(d.b)(t.value)){var i=t.value;Object(d.a)(i)&&i.forEach(function(e){e&&e.forEach(function(e){e&&(e.value=e.fieldValue,delete e.fieldValue)})}),t.value=e.getItemValue(t,null,"update")}Object(d.a)(t.value)||(t.value=[])}t.show&&n.push(t)}),e.postList=[{name:"岗位信息",list:n}];var o=t.data.employeeQuitInfo;if(e.leaveDetail=o,o){var u=[];s.a.getFields(o).forEach(function(e){var t={label:e.name};"quitType"==e.field?t.value=s.a.quitTypeValue[o[e.field]]:"quitReason"==e.field?t.value=s.a.quitReasonValue[o[e.field]]:t.value=o[e.field],u.push(t)}),e.leaveList=[{name:"离职信息",list:u}]}else e.leaveList=[]}).catch(function(){e.loading=!1})}}},m=(i("v3P6"),i("KHd+")),p=Object(m.a)(f,function(){var e=this.$createElement,t=this._self._c||e;return t("div",{directives:[{name:"loading",rawName:"v-loading",value:this.loading,expression:"loading"}]},[t("wk-custom-base-detail",{attrs:{list:this.postList}}),this._v(" "),this.leaveList.length>0?t("wk-base-detail",{attrs:{list:this.leaveList}}):this._e()],1)},[],!1,null,"3cfeafd6",null);p.options.__file="EmployeePostInfo.vue";var h=p.exports,v=i("4d7F"),b=i.n(v),y=i("GQeE"),g=i.n(y),_=i("QbLZ"),w=i.n(_),k=i("cHj4"),F=i("EP+0"),x=i("lMh0"),C=i("REUC"),T=i("XaGJ"),V=i("TN+K"),L=i("jzeO"),D=i("V4FA"),E=i("L2JU"),A={name:"EmployeeBaseInfo",components:{WkBaseDetail:n.a,CreateSections:F.a,WkBaseDetailSection:x.a,FormAddDialog:T.a,WkReminder:L.a,WkCustomBaseDetail:o.a,WkCustomBaseDetailSection:C.a,CustomFormAddDialog:V.a},filters:{},mixins:[u.a],props:{},data:function(){return{loading:!1,baseEditAuth:!1,employeeDetail:null,communicationEditAuth:!1,baseEditList:[],baseList:[],communicationEditList:[],communicationList:[],educationBaseList:[],educationList:[],workBaseList:[],workList:[],certificateBaseList:[],certificateList:[],trainingBaseList:[],trainingList:[],contactsBaseList:[],contactsFieldList:[],contactsAddRules:{},contactsList:[],formAddTitle:"",formAddForm:{},formAddRules:{},formAddFields:[],formAddDialogShow:!1,customFormAddFields:[],customFormAddDialogShow:!1,updateKey:Date.now()}},computed:w()({},Object(E.b)(["hrmUserInfo"]),{reminderContent:function(){return this.baseEditAuth||this.communicationEditAuth?"可编辑的信息由公司管理员设置，如有问题，请联系公司管理员。":"您的编辑权限已被管理员关闭，如有问题，请联系公司管理员。"},communicationDropdownItems:function(){var e=[];return this.communicationEditAuth&&e.push({label:"编辑",command:"edit",icon:""}),e},employeeId:function(){return this.hrmUserInfo?this.hrmUserInfo.employeeId:""}}),watch:{},mounted:function(){this.getEmployDetail(),this.getDetail()},methods:{getDetail:function(){var e=this;this.loading=!0,Object(l.a)({url:"hrmEmployeeArchives/personalArchives",method:"post"}).then(function(t){e.loading=!1;var i=t.data||{},a=Object(r.w)(i);e.baseList=[{name:"基本信息",list:e.getCustomFieldListValue(i.information||[],"base")}],e.communicationList=[{name:"通讯信息",list:e.getCustomFieldListValue(i.communicationInformation||[],"communication")}],e.baseEditList=a.information||[],e.communicationEditList=a.communicationInformation||[];var l=i.educationExperienceList||[],n=[];e.educationBaseList=l,l.forEach(function(e){var t=[];c.c.fields.forEach(function(i){var a={};a.label=i.name,"education"===i.field?a.value=c.c.educationValue[e[i.field]]:"teachingMethods"===i.field?a.value=c.c.teachingMethodsValue[e[i.field]]:"isFirstDegree"===i.field?a.value=c.c.isFirstDegreeValue[e[i.field]]:a.value=e[i.field],t.push(a)}),n.push(t)}),e.educationList=n;var o=i.workExperienceList||[];e.workBaseList=o,e.workList=e.getCommonFieldListValue(o,c.g.fields);var s=i.certificateList||[];e.certificateBaseList=s,e.certificateList=e.getCommonFieldListValue(s,c.a.fields);var d=i.trainingExperienceList||[];e.trainingBaseList=d,e.trainingList=e.getCommonFieldListValue(d,c.f.fields);var u=i.hrmEmployeeContacts||[];e.contactsBaseList=a.hrmEmployeeContacts||[],e.contactsList=u.map(function(t){return e.getCustomFieldListValue(t.information||[])})}).catch(function(){e.loading=!1})},getEmployDetail:function(){var e=this;Object(k.H)().then(function(t){e.employeeDetail=t.data}).catch(function(){})},getCustomFieldListValue:function(e,t){var i=this,a=!1;e.forEach(function(e){a||1!==e.isEmployeeUpdate||(a=!0),["id_type","sex","birthday_type","highest_education"].includes(e.fieldName)?e.value=e.fieldValueDesc:e.value=e.fieldValue,delete e.fieldValue}),"base"===t?this.baseEditAuth=a:"communication"===t&&(this.communicationEditAuth=a);var l=this.getFormAssistIds([e]),n=[];return e.forEach(function(e){if(i.getFormItemDefaultProperty(e,!1),e.show=!l.includes(e.formAssistId),"detail_table"===e.formType){if(!Object(d.b)(e.value)){var t=e.value;Object(d.a)(t)&&t.forEach(function(e){e&&e.forEach(function(e){e&&(e.value=e.fieldValue,delete e.fieldValue)})}),e.value=i.getItemValue(e,null,"update")}Object(d.a)(e.value)||(e.value=[])}e.show&&n.push(e)}),n},getCommonFieldListValue:function(e,t){var i=[];return e.forEach(function(e){var a=[];t.forEach(function(t){var i={};i.label=t.name,i.value=e[t.field],a.push(i)}),i.push(a)}),i},formAddPass:function(){this.uploadCustomRelativeData()},formAddChange:function(e,t,i,a){a&&(e.valueList=a)},customFormAddPass:function(e){var t=this,i=Object(r.w)(e.field);i.forEach(function(e){if("native_place"==e.fieldName)if(e.value){var t="";g()(e.value).forEach(function(i){e.value[i]&&(t?t+=","+e.value[i]:t=e.value[i])}),e.value=t}else e.value="";if(45===e.type){var i=e.value;Object(d.a)(i)&&i.forEach(function(e){e&&e.forEach(function(e){e&&(e.fieldValue=e.value,delete e.value)})})}delete e.fieldType,e.fieldValue=e.value,delete e.value});var a=this.$refs.customFormAdddialog;a.loading=!0;var l={};l.employeeId=this.id,l.dataList=i;var n=null;"编辑基本信息"===this.formAddTitle?n=k.T:"编辑通讯信息"===this.formAddTitle&&(n=k.S),n(l).then(function(e){a.loading=!1,t.$message.success(t.formAddTitle+"成功"),t.customFormAddDialogShow=!1,t.getDetail(),t.updateKey=Date.now(),t.$emit("handle",{type:"update"})}).catch(function(){a.loading=!1})},baseCommandSelect:function(e){var t=this;if("edit"==e){var i=[];Object(r.w)(this.baseEditList).forEach(function(e){if("age"!=e.fieldName){if(t.handleFieldExtendList(e),D.a.getCreateFieldDefalutData(e),e.value=e.fieldValue,delete e.fieldValue,t.handleTableValue(e),"city"==e.formType)if(e.value){var a=e.value.split(",");e.value={province:a[0]||"",city:a[1]||""}}else e.value={};i.push(e)}}),this.formAddTitle="编辑基本信息",this.customFormAddFields=i,this.customFormAddDialogShow=!0}},handleTableValue:function(e){if("detail_table"===e.formType){if(!Object(d.b)(e.value)){var t=e.value;Object(d.a)(t)&&t.forEach(function(e){e&&e.forEach(function(e){e&&(D.a.getCreateFieldDefalutData(e),e.value=e.fieldValue,delete e.fieldValue)})})}Object(d.a)(e.value)||(e.value=[])}},handleFieldExtendList:function(e){if("detail_table"===e.formType&&!Object(d.b)(e.fieldExtendList)){var t=e.fieldExtendList;Object(d.a)(t)&&t.forEach(function(e){e&&e&&(D.a.getCreateFieldDefalutData(e),e.value=e.fieldValue,delete e.fieldValue)})}},communicationCommandSelect:function(e){var t=this;if("edit"==e){var i=[];Object(r.w)(this.communicationEditList).forEach(function(e){t.handleFieldExtendList(e),D.a.getCreateFieldDefalutData(e),e.value=e.fieldValue,delete e.fieldValue,t.handleTableValue(e),i.push(e)}),this.formAddTitle="编辑通讯信息",this.customFormAddFields=i,this.customFormAddDialogShow=!0}},UniquePromise:function(e){var t=this,i=e.field,a=e.value;return new b.a(function(e,l){Object(k.v)({fieldId:i.fieldId,id:t.employeeDetail?t.employeeDetail.employeeId:"",value:a}).then(function(t){e()}).catch(function(){l()})})},uploadCustomRelativeData:function(){var e=this;this.$refs.formAdddialog.loading=!0;var t={employeeId:this.id};this.formAddFields.forEach(function(t){if(t.oldFieldValueDesc=t.fieldValueDesc,"city"==t.formType){var i=e.formAddForm[t.field]||{};t.fieldValue=i.province?i.province+","+i.city:""}else t.fieldValue=e.formAddForm[t.field];"user"==t.formType?t.valueList?t.fieldValueDesc=t.valueList.map(function(e){return e.employeeName}).join("，"):t.fieldValue&&!t.valueList&&(t.fieldValueDesc=t.fieldValueDesc):"structure"==t.formType?t.valueList?t.fieldValueDesc=t.valueList.map(function(e){return e.name}).join("，"):t.fieldValue&&!t.valueList&&(t.fieldValueDesc=t.fieldValueDesc):"id_type"==t.field?t.fieldValueDesc=c.d.idTypeValue[t.fieldValue]:"sex"==t.field?t.fieldValueDesc=c.d.sexValue[t.fieldValue]:"birthday_type"==t.field?t.fieldValueDesc=c.d.birthdayTypeValue[t.fieldValue]:"highest_education"==t.field?t.fieldValueDesc=c.c.educationValue[t.fieldValue]:t.fieldValueDesc=t.fieldValue}),t.dataList=this.formAddFields;var i=null;"编辑基本信息"===this.formAddTitle?i=k.T:"编辑通讯信息"===this.formAddTitle&&(i=k.S),i(t).then(function(t){e.$refs.formAdddialog.loading=!1,e.$message.success(e.formAddTitle+"成功"),e.formAddDialogShow=!1,e.getDetail()}).catch(function(){e.$refs.formAdddialog.loading=!1})}}},S=(i("eGUX"),Object(m.a)(A,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}]},[i("wk-reminder",{staticStyle:{margin:"-15px 10px 10px"},attrs:{content:e.reminderContent}}),e._v(" "),i("wk-custom-base-detail",{attrs:{list:e.baseList},scopedSlots:e._u([{key:"data",fn:function(t){var i=t.data;return[e._v(e._s(i.value))]}}])},[e.baseEditAuth?i("el-button",{attrs:{slot:"more",type:"text"},on:{click:function(t){e.baseCommandSelect("edit")}},slot:"more"},[e._v("编辑")]):e._e()],1),e._v(" "),i("wk-custom-base-detail",{attrs:{"dropdown-items":e.communicationDropdownItems,list:e.communicationList},on:{"top-command-select":e.communicationCommandSelect}}),e._v(" "),i("create-sections",{attrs:{title:"教育经历"}},[0===e.educationList.length?i("div",{staticClass:"empty-text"},[e._v("暂无数据")]):e._l(e.educationList,function(e,t){return i("wk-base-detail-section",{key:t,attrs:{list:e}})})],2),e._v(" "),i("create-sections",{attrs:{title:"工作经历"}},[0===e.workList.length?i("div",{staticClass:"empty-text"},[e._v("暂无数据")]):e._l(e.workList,function(e,t){return i("wk-base-detail-section",{key:t,attrs:{list:e}})})],2),e._v(" "),i("create-sections",{attrs:{title:"证书/证件"}},[0===e.certificateList.length?i("div",{staticClass:"empty-text"},[e._v("暂无数据")]):e._l(e.certificateList,function(e,t){return i("wk-base-detail-section",{key:t,attrs:{list:e}})})],2),e._v(" "),i("create-sections",{attrs:{title:"培训经历"}},[0===e.trainingList.length?i("div",{staticClass:"empty-text"},[e._v("暂无数据")]):e._l(e.trainingList,function(e,t){return i("wk-base-detail-section",{key:t,attrs:{list:e}})})],2),e._v(" "),i("create-sections",{attrs:{title:"联系人"}},[0===e.contactsList.length?i("div",{staticClass:"empty-text"},[e._v("暂无数据")]):e._l(e.contactsList,function(e,t){return i("wk-custom-base-detail-section",{key:t,attrs:{list:e}})})],2),e._v(" "),i("form-add-dialog",{ref:"formAdddialog",attrs:{title:e.formAddTitle,form:e.formAddForm,rules:e.formAddRules,fields:e.formAddFields,visible:e.formAddDialogShow},on:{"update:form":function(t){e.formAddForm=t},"update:visible":function(t){e.formAddDialogShow=t},pass:e.formAddPass,change:e.formAddChange}}),e._v(" "),e.customFormAddDialogShow?i("custom-form-add-dialog",{ref:"customFormAdddialog",attrs:{id:e.employeeId,title:e.formAddTitle,fields:e.customFormAddFields,visible:e.customFormAddDialogShow},on:{"update:visible":function(t){e.customFormAddDialogShow=t},pass:e.customFormAddPass}}):e._e()],1)},[],!1,null,"4108aa1a",null));S.options.__file="EmployeeBaseInfo.vue";var j=S.exports,O={name:"MyArchives",components:{XrHeader:a.a,EmployeePostInfo:h,EmployeeBaseInfo:j},props:{},data:function(){return{loading:!1,detailData:null,tabNames:[{label:"基本信息",name:"EmployeeBaseInfo"},{label:"岗位信息",name:"EmployeePostInfo"}],tabCurrentName:"EmployeeBaseInfo"}},computed:{},watch:{},created:function(){},mounted:function(){},beforeDestroy:function(){},methods:{}},I=(i("5OJy"),Object(m.a)(O,function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"my-archives"},[i("xr-header",{staticStyle:{padding:"0px 15px 15px 0"},attrs:{"ft-top":"0","icon-class":"wk wk-archive","icon-color":"#2362FB",label:"我的档案"}}),e._v(" "),i("el-tabs",{staticClass:"side-detail__tabs--default",model:{value:e.tabCurrentName,callback:function(t){e.tabCurrentName=t},expression:"tabCurrentName"}},e._l(e.tabNames,function(e,t){return i("el-tab-pane",{key:t,attrs:{label:e.label,name:e.name,lazy:""}},[i(e.name,{tag:"component",staticClass:"side-detail-tabs-content",staticStyle:{padding:"15px 10px","margin-left":"-20px"}})],1)}))],1)},[],!1,null,"629b65d0",null));I.options.__file="index.vue";t.default=I.exports},vpoJ:function(e,t,i){},zIzm:function(e,t,i){"use strict";var a=i("ihDC");i.n(a).a}}]);