let common = {
  validate : {
    checkRequired : function(className){
      let vflag = true;
      $(className).each(function(i,e){
        const field = $(this).val();
        const title = $(this).attr("title");
        if(field == null || field == ''){
          alert(title+"값은 필수입니다.");
          $(this).focus();
          vflag = false;
          return false;
        }
      });
      return vflag;
      },
    password : function($password) {
      let passwordInput = $password.val();
      if(passwordInput != null){
        let passwordLength = passwordInput.length;
        if(passwordLength > 4){
          alert("비밀번호는 4자이상 입력할 수 없습니다.");
          $password.val("");
        }else{
          let replaceInput = passwordInput.replace(/[^0-9]/g, '');
          if(passwordInput != replaceInput){
            alert("비밀번호는 숫자만 입력가능합니다.");
          }
          $password.val(replaceInput);
        }
      }
    }
  }
};