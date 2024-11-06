# java-convenience-store-precourse

## ✅ 구현할 기능 목록

### 1. 입력
- [ ] 상품 구매 안내 메시지에 대한 사용자의 입력을 받는다.
  - [ ] 구매할 상품과 수량을 입력 받는다. (형식: [상품명-수량],[상품명-수량])


- [ ] 프로모션 상품이지만, 고객이 해당 수량만큼 가져오지 않았을 경우에 대한 사용자의 입력을 받는다.
  - Y: 증정 상품을 추가한다.
  - N: 증정 상품을 추가하지 않는다.
  - 해당 수량보다 많이 가져왔을때는?


- [ ] 프로모션 재고가 부족해 일부 수량을 혜택 없이 결제해야 하는 경우에 대한 사용자의 입력을 받는다.
  - Y: 일부 수량에 대해 정가로 결제한다.
  - N: 정가로 결제해야하는 수량만큼 제외한 후 결제한다.


- [ ] 멤버십 할인 적용 여부에 대한 사용자의 입력을 받는다.
  - Y: 멤버십 할인을 적용한다.
  - N: 멤버십 할인을 적용하지 않는다.


- [ ] 추가 구매 여부에 대한 사용자의 입력을 받는다.
  - Y: 이전 구매 이력을 바탕으로 업데이트된 상품 목록을 기준으로 추가 구매를 진행한다.
  - N: 구매 프로세스를 종료한다.
  - [ ] 사용자가 추가 구매 여부에 N를 입력하기 전까지 위 출력과 입력이 계속 반복되어야 한다.

<br>

### 2. 출력
- [x] 환영인사를 출력한다.
  - ```text
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
    ```
- [x] 상품명, 가격, 재고, 프로모션 이름을 안내(출력)한다.
  - [x] `src/main/resources/products.md` 파일을 이용한다.
  - [x] 재고가 0개라면 `재고 없음`을 출력한다.
  - ```text
    - 콜라 1,000원 10개 탄산2+1
    - 콜라 1,000원 10개
    - 사이다 1,000원 8개 탄산2+1
    - 사이다 1,000원 7개
    - 오렌지주스 1,800원 9개 MD추천상품
    - 오렌지주스 1,800원 재고 없음
    - 탄산수 1,200원 5개 탄산2+1
    - 탄산수 1,200원 재고 없음
    - 물 500원 10개
    - 비타민워터 1,500원 6개
    - 감자칩 1,500원 5개 반짝할인
    - 감자칩 1,500원 5개
    - 초코바 1,200원 5개 MD추천상품
    - 초코바 1,200원 5개
    - 에너지바 2,000원 5개
    - 정식도시락 6,400원 8개
    - 컵라면 1,700원 1개 MD추천상품
    - 컵라면 1,700원 10개
    ```
- [ ] 상품 구매 안내 메시지를 출력한다.
  - ```text
    구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    ```
- [ ] 프로모션 상품이지만, 고객이 해당 수량만큼 가져오지 않았을 경우 혜택 안내 메시지를 출력한다.
  - ```text
    현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    ```
- [ ] 프로모션 제고가 부족하여 일부 수량을 혜택 없이 결제해야 하는 경우, 안내 메시지를 출력한다.
  - ```text
    현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    ```
- [ ] 멤버십 할인 적용 여부를 확인하는 메시지를 출력한다.
  - ```text
    멤버십 할인을 받으시겠습니까? (Y/N)
    ```
- [ ] 영수중을 출력한다.
  - [ ] 구매 상품 내역 (상품명, 수량, 금액)
  - [ ] 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품 목록 (상품명, 수량)
  - [ ] 금액 정보
    - 총구매액: 구매한 상품의 총 수량과 총 금액
    - 행사할인: 프로모션에 의해 할인된 금액
    - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    - 내실돈: 최종 결제 금액
  - ```text
    ===========W 편의점=============
    상품명		수량	금액
    콜라		3 	3,000
    에너지바 		5 	10,000
    ===========증	정=============
    콜라		1
    ==============================
    총구매액		8	13,000
    행사할인			-1,000
    멤버십할인			-3,000
    내실돈			 9,000
    ```
- [ ] 추가 구매 여부를 확인하는 메시지를 출력한다.
  - ```text
    감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
    ```
 
<br>

### 3. 재고 관리

- [ ] 매 결제 시 각 상품의 재고 수량과 구매 수량을 비교하여 결제 가능 여부를 판단한다.
- [ ] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 재고를 실시간으로 업데이트한다.

<br>

### 4. 프로모션 할인

- [ ] 오늘 날짜가 프로모션 기간 내에 포함되어 있는지 확인한다.
- [ ] 프로모션은 N+1의 형태로 진행된다 (N개 구매시 1개 무료 증정)
- [ ] 프로모션은 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
- [ ] 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
  - 같은 상품이여도 프로모션이 적용된 상품이 있을 수 있고, 아닌게 있을 수 있다.
- [ ] 프로모션 기간 중이라면, 프로모션 재고를 우선적 차감, 재고가 부족할 경우에는 일반 재고를 사용한다.
  - 프로모션 재고가 아니더라도 수량이 부족하면 일반 재고를 사용할 수 있다.
    - 일반 재고를 사용하는 경우 정가로 계산해야한다.
    - ex_1) 1+2인데 고객이 10개 구매, 남은 프로모션 재고는 7개 => 6개는 프로모션 할인, 나머지 4개는 정가 계산
    - ex_2) 일반 재고까지 합쳐도 수량이 안된다면 `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.` 처리
- [ ] 프로모션 상황의 예외에 대해 고객에게 안내해야한다.
  - 프로모션 개수보다 적게 가져온 경우
  - 프로모션 재고가 부족한 경우

<br>

### 5. 멤버십 할인

- [ ] 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
- [ ] 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- [ ] 멤버십 할인의 최대 한도는 8,000원이다.
  - 30% 계산 시 8,000을 넘어가면 할인가를 8,000으로 고정한다.

<br>

### 6. 영수증 출력

- 구현할 기능 목록 > 출력 > 영수중을 출력한다. 와 동일

<br>

### 7. 에러 처리
- [ ] 사용자가 잘못된 값을 입력했을 때, "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
  - [ ] 구매할 상품과 수량 형식이 올바르지 않은 경우: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
  - [ ] 존재하지 않는 상품을 입력한 경우: `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
  - [ ] 구매 수량이 재고 수량을 초과한 경우: `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
  - [ ] 기타 잘못된 입력의 경우: `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`
