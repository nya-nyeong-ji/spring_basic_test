# Step-02 validate, 예외처리
API 를 진행하면 프론트에게서 받은 값에대한 유효성 검사를 진행하게 됩니다.<br>
전당된 값에 대한 유효성 검사는 매우 반복적으로 일어나는 과정이기 때문에 이를 효과적으로 처리한다면 전체 효율성이 올라가게 됩니다.

## Point
* @Valid 를 이용한 유효성 검사
* @ControllerAdvice 를 이용한 Exception 관리
* Error 메시지 통합

## @Valid 를 이용한 유효성 검사
### DTO 에 유효성 검사 어노테이션 추가
```java
@Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {
        @NotEmpty
        private String id;
        @NotEmpty
        private String nickname;
        @Email
        private String email;
        @NotEmpty
        private String password;
        
        ...
    }
```
이전 단계에서 작성했던 signUpReq 에 @Email, @NotEmpty 등의 유효성 어노테이션을 추가합니다.<br>
이 밖에도 @NotNull, @Pattern 등의 여러가지 어노테이션을 통해 유요성을 검사할 수 있습니다.<br>
아래의 컨트롤러에서 @Valid 어노테이션을 통해서 유효성 검사를 진행합니다.<br>
유효성 검사를 실패하면 MethodArgumentNotValidException 예외가 발생합니다

```java
public class MemberController {

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public MemberDto.Res signUp(@RequestBody @Valid final MemberDto.SignUpReq dto) {
        return new MemberDto.Res(memberService.create(dto));
    }
}
```
컨트롤러에 @Valid 어노테이션을 추가했습니다.<br>
MemberSignUpReq 클래스의 유효성 검사가 실패할 경우 MethodArgumentNotValidException 예외가 발생합니다.<br>
이러한 과정은 핵심 비지니스 로직이 아님에도 주기적으로 반복되며 많은 시간을 할애하게 됩니다.<br>
다음으로 MethodArgumentNotValidException 예외가 발생시에 공통적으로 사용자에게 적절한 Response 값을 리턴해 주는 작업을 진행하겠습니다

## @ControllerAdvice 를 이용한 Exception 관리
```java
@ControllerAdvice
@ResponseBody
public class ErrorExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        return buildFieldErrors(ErrorCode.INPUT_INVALID, fieldErrors);
    }
}
```
@ControllerAdvice 어노테이션을 추가하면 특정 Exception 을 핸들링하여 적절한 값을 Response 값으로 리턴해 줍니다.<br>
MethodArgumentNotValidException 을 핸들링하지 않을 경우 필요하지 않은 오류정보들까지 전송하기 떄문에 이는 바람직하지 않습니다.<br>
또한 자체적으로 전송하는 Response 결과를 엔드에서 처리하는 것이기 때문에 항상 일정한 포멧을 유지해야 합니다.

### MethodArgumentNotValidException 의 Response 처리
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    final List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
    return buildFieldErrors(ErrorCode.INPUT_INVALID, fieldErrors);
}
private List<ErrorResponse.FieldError> getFieldErrors (BindingResult bindingResult) {
    final List<FieldError> errors = bindingResult.getFieldErrors();
    return errors.parallelStream()
    .map(error -> ErrorResponse.FieldError.builder()
    .reason(error.getDefaultMessage())
    .field(error.getField())
    .value((String) error.getRejectedValue())
    .build())
    .collect(Collectors.toList());
}

private ErrorResponse buildFieldErrors(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
    return ErrorResponse.builder()
    .code(errorCode.getCode())
    .status(errorCode.getStatus())
    .message(errorCode.getMessage())
    .errors(errors)
    .build();
}
```

발생할 수 있는 동일한 예외에 대하여 Response 를 만들어 관리할 경우 다음과 같은 장점을 얻을 수 있습니다.
* 중복적으로 발생하는 예외를 관리하기 편합니다.
* 예외 발생 시의 메시지를 관리하기 편합니다.

### ErrorCode
```java
@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND("AC_001", "해당 멤버를 찾을 수 없습니다.", 404),
    ID_DUPLICATION("AC_002", "아이디가 중복되었습니다.", 400),
    INPUT_INVALID("???", "올바르지 못한 입력입니다.", 400);

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
```
발생할 수 있는 에러들을 이렇게 코드로 관리하는 것도 좋은 방법이라는 의견을 받아 만들어본 ErrorCode 클래스입니다.<br>
발생할 수 있는 에러들을 완벽히 관리할 수 있다면 404 : Not Found 와 같은 직관성을 가지게 될 것 같습니다.<br>
또한 이렇게 에러를 한 곳에 모아두면 에러 메시지 등을 관리하기 편합니다.

## 단점
* 모든 RequestDto 에 대해 어노테이션 검사가 진행됩니다.
* 유효성 검사 로직이 변경될 경우 모든 곳에서 변경이 일어납니다.