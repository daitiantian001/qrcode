class DdLogin {
    constructor(data,callBack){
        this.el = data.el;
        this.appId = data.appId;
        this.redirectUri = data.redirectUri;
        this.responseType = data.responseType;
        this.scope = data.scope;
        this.state = data.state;
        this.callBack = callBack;
        this.width = data.width;
        this.height = data.height;
        this.createIframe();
    }
    createIframe(){
        console.log(this.el);
        var iframe = document.createElement('iframe');
        iframe.src=`http://192.168.1.28:8301/ddLogin.html?appId=${this.appId}&redirectUri=${this.redirectUri}&responseType=${this.responseType}&scope=${this.scope}&state=${this.state}&width=${this.width}&height=${this.height}`;
        iframe.width = this.width,
        iframe.height = this.height;
        iframe.frameBorder = 0;
        this.el.innerHTML = '';
        this.el.appendChild(iframe);
    }
}
