var AesUtil = function(pass) {
  this.keySize = 128 / 32;
  this.iterationCount = 500;
  var iv =   CryptoJS.MD5(pass).toString(CryptoJS.enc.Hex);
  // var salt = CryptoJS.SHA1("Message"); 
  var salt = CryptoJS.SHA1(pass).toString(CryptoJS.enc.Hex);
  var keypass = pass;
  //FUNCION PARA GENERAR LA CLAVE
  this.generateKey = function(passPhrase) {
    var key = CryptoJS.PBKDF2(
        passPhrase, 
        CryptoJS.enc.Hex.parse(salt),
        { keySize: this.keySize, iterations: this.iterationCount });
    return key;
  }
  var key = this.generateKey(pass);
  //FUNCION PARA ENCRIPTAR
  this.encrypt = function(plainText) {
    var encrypted = CryptoJS.AES.encrypt(
        plainText,
        key,
        { iv: CryptoJS.enc.Hex.parse(iv) });
    return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
  }
  //FUNCION PARA DESENCRIPTAR
  this.decrypt = function(cipherText) {
    var cipherParams = CryptoJS.lib.CipherParams.create({
      ciphertext: CryptoJS.enc.Base64.parse(cipherText)
    });
    var decrypted = CryptoJS.AES.decrypt(
        cipherParams,
        key,
        { iv: CryptoJS.enc.Hex.parse(iv) });
    return decrypted.toString(CryptoJS.enc.Utf8);
  }
  this.getKey = function(){
    return keypass;
  }
  this.getSalt = function(){
    return salt;
  }
  this.getIv = function(){
    return iv.toString();
  }
};