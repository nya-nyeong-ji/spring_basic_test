# step-06 Setter 사용 금지

객체지향 언어를 다룰 때에 빠짐없이 나오는 것이 있습니다.<br>
Getter, Setter 입니다.<br>
하지만 무분별한 Setter 의 사용은 객체지향적 관점에 어울리지 않습니다.

## Point
* Setter 메소드의 단점
* Setter 메소드를 사용하지 않는 방법

## Setter 메소드의 단점

### 의도를 가지기 힘들다.
Setter 메소드를 이용하여 update 를 진행할 경우 아래와 같은 형태를 취하게 됩니다.
```java
public Example update(int target1, int target2, int target3) {
    ...
    Example.setTarget1(target1);
    Example.setTarget2(target2);
    Example.setTarget3(target3);
    ...
}
```

단순히 정보의 변경을 원하는 코드들이 나열되어 있습니다<br>
이 상태에서는 무엇을 위해 변경하려는 것인지 알기 힘듭니다.

### 객체의 일관성을 유지하기 힘들다.
Setter 메소드를 사용할 경우 객체를 언제 어디에서든 변경할 수 있게 됩니다.<br>
개발적 편의성은 향상될 수 있으나 객체의 일관성을 유지하기 힘들어집니다.<br>
그냥 Setter 메소드를 만들지 않는 것도 한 가지 방법이지만 관례적으로 만들어지기도 하거니와<br>
이러한 과정에서 실수를 줄일 수 있습니다.


## Setter 메소드를 사용하지 않는 방법

### updateExample
```java
public Example updateExample(long id, ExampleDto dto) {
    final Example example = findById(id);
    example.update(dto);
    return example;
}

public void update(ExampleDto dto) {
    this.target1 = dto.getTarget1();
    this.target2 = dto.getTarget2();
    this.target3 = dto.getTarget3();
}
```
findById 메소드를 이용한 영속성을 가진 객체를 가져와서 도메인에 작성된 update 를 진행합니다.

### createExample
```java
public static class Example {
    ...
    
    @Builder
    public Example(int target1, int target2, int target3) {
        this.target1 = target1;
        this.target2 = target2;
        this.target3 = target3;
    }
    ...
}
```

빌드 패턴을 이용할 경우 Setter 를 사용하지 않고서도 객체를 생성할 수 있습니다.