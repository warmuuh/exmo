package wrm.exmo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor(staticName = "of") @ToString
public @Data class Pair<TFirst, TSecond>{
	TFirst first; 
	TSecond second;
}