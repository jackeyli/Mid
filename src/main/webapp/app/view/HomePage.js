Ext.define('app.view.HomePage', {
    extend: 'Ext.panel.Panel',
    views:['Course','LoginWindow','NewSemesterWindow'],
    alias:'widget.homePage',
    width:'100%',
    height:'100%',
    plain:true,
    pageTitle:'Home Page',
    tbar:[{
        text: 'Course',
        menu:{
            xtype:'menu',
            items:[
                {
                    text:'View Course',
                    handler:function(){
                        if(!window.UserInfo){
                            alert('You cannot enter this page');
                            return;
                        }
                        window.location.search = '?page=app.view.Course';
                    }
                },
                {
                    text:'View Registed Course',
                    handler:function(){
                        if(!window.UserInfo || UserInfo.userType != 'STUDENT'){
                            alert('You cannot enter this page');
                            return;
                        }
                        window.location.search = '?page=app.view.RegistedCourse';
                    }
                }
            ]
        }
    },{
        text: 'Assignment',
        menu:[
            {
                text:'View Assignment',
                handler:function(){
                    if(!window.UserInfo || (UserInfo.userType != 'STUDENT' && UserInfo.userType != 'TEACHER')){
                        alert('You cannot enter this page');
                        return;
                    }
                    window.location.search = '?page=app.view.Assignment';
                }
            }
        ]
    },{
        text: 'ScholarShip',
        menu:[
            {
                text:'View Scholar Ship',
                handler:function(){
                    if(!window.UserInfo || (UserInfo.userType != 'FINANCEMANAGER')){
                        alert('You cannot enter this page');
                        return;
                    }
                    window.location.search = '?page=app.view.ScholarShip';
                }
            }
        ]
    },{
        text: 'Create New Semester',
        handler:function(){
            if(!window.UserInfo || (UserInfo.userType != 'COURSEMANAGER')){
                alert('You cannot enter this page');
                return;
            }
            Ext.create('app.view.SemesterWindow').show();
        }
    },{
        text:'Log In',
        handler:function(){
            Ext.create('app.view.LoginWindow').show();
        }
    },{
        text:'Log Out',
        handler:function(){
            Ext.Ajax.request({
                scope:this,
                params:{},
                url:'request/login/logout',
                success:function(response){
                    responseWrapper((resp)=>{
                        document.getElementById('welcomeUser').innerText = '';
                        alert(resp);
                })(response);
                }
            });
        }
    }],
    id:'HomePage',
    align:'stretch',
    items:[
        {
            xtype:'panel',
            itemId:'contentPage',
            flex:1,
            layout:'fit',
            items:[]
        }
    ],
    listeners:{
        'afterrender':function(){
            var page = getWindowParam('page');
            if(page) {
                this.switchPage(page);
            }
        }
    },
    switchPage:function(page){
        this.getComponent('contentPage').removeAll(true);
        if(window.UserInfo) {
            var nPage = Ext.create(page);
            document.title = nPage.pageTitle;
            this.getComponent('contentPage').add(nPage)
        }
    }
});