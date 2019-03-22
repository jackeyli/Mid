Ext.define('app.store.CourseStore',{
    extend:'Ext.data.Store',
    autoLoad:false,
    fields:['id','name','teacherName','credit','registedCount','teacherID','semesterName','semesterID','schedule','capacity','courseType','location','url','rule','version']
});