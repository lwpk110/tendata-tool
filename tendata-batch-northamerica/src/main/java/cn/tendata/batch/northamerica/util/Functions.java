package cn.tendata.batch.northamerica.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public abstract class Functions {

    private static Pattern indexPattern = Pattern.compile("\\S+_(\\d+)_\\d+");
    private static String KEY_CONTAINER_NO = "ContainerNumber_\\d+_20";
    private static String KEY_CONTAINER_SIZE = "ContainerLength_\\d+_20";
    private static String KEY_CONTAINER_SEAL_NO1 = "SealNumber1_\\d+_20";
    private static String KEY_CONTAINER_SEAL_NO2 = "SealNumber2_\\d+_20";


    /**
     * teu v1.0
     *
     */
    @Deprecated
    public static BigDecimal calculateTeu(String containerNo, Float weight, Float totalWeight){
        return calculateTeu(containerNo, weight, totalWeight, 2);
    }

    public static BigDecimal calculateTeu(String containerNo, Float weight, Float totalWeight, int scale){
        BigDecimal teu = BigDecimal.ZERO;
        if(StringUtils.hasText(containerNo)){
            String[] values = StringUtils.delimitedListToStringArray(containerNo, ";");
            if(totalWeight != null && totalWeight > 0){
                teu = BigDecimal.valueOf(values.length * (weight / totalWeight));
            }
            else{
                teu = BigDecimal.valueOf(values.length);
            }
        }
        return teu.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * teu v1.1
     */
    public static BigDecimal calculateTeu(Map<String,List<String>> containerNoMap,Map<String,List<String>> containerLengthMap){
        if(!CollectionUtils.isEmpty(containerLengthMap)){
            final int[] total = {0};
            containerLengthMap.forEach((String key, List<String> containerLengthList) ->{
                if(!CollectionUtils.isEmpty(containerLengthList)){
                    final String containerIndex = getIndexOfContainer(key);
                    if(!StringUtils.isEmpty(containerIndex)){
                        String containerNoKey = KEY_CONTAINER_NO.replace("\\d+",containerIndex);
                        List<String> containerNoList = containerNoMap.get(containerNoKey);
                        IntStream.range(0,containerLengthList.size()).forEach(value -> {
                            String currentContainerNo = containerNoList.get(value);
                            String currentContainerLength = containerLengthList.get(value);
                            int currentTeu = teuStrategy(currentContainerNo,currentContainerLength);
                            total[0] += currentTeu;
                        });
                    }
                }
            } );
            return BigDecimal.valueOf(total[0]);
        }
        return BigDecimal.ZERO;
    }

    private static int teuStrategy(String containerNo,String len){
        int base = 2000;
        if (hasContainerLength(len)){
            int containerLength =  NumberUtils.parseNumber(len,Integer.class);
            if(containerLength <= base){
                return   1;
            }else if(containerLength > base){
                return  2;
            }
        }else{ //len ==0 has containerNo teu =1
            if(hasContainer(containerNo)){
                return 1;
            }
        }
        return 0;
    }


    public static BigDecimal normalizeWeight(Float weight, String weightUnit){
        return normalizeWeight(weight, weightUnit, 2);
    }
    
    public static BigDecimal normalizeWeight(Float weight, String weightUnit, int scale){
        BigDecimal unifiedWeight = BigDecimal.valueOf(weight);
        if("LB".equalsIgnoreCase(weightUnit) || "L".equalsIgnoreCase(weightUnit)){
            unifiedWeight = BigDecimal.valueOf(weight * 0.4536F);
        }
        if("LT".equalsIgnoreCase(weightUnit) || "T".equalsIgnoreCase(weightUnit)){
            unifiedWeight = BigDecimal.valueOf(weight * 1016);
        }
        if("ST".equalsIgnoreCase(weightUnit) || "S".equalsIgnoreCase(weightUnit)){
            unifiedWeight = BigDecimal.valueOf(weight * 907.19F);
        }
        if("ET".equalsIgnoreCase(weightUnit) || "E".equalsIgnoreCase(weightUnit)){
            unifiedWeight = BigDecimal.valueOf(weight * 1000);
        }
        if("GT".equalsIgnoreCase(weightUnit) || "G".equalsIgnoreCase(weightUnit)){
            unifiedWeight = BigDecimal.valueOf(weight / 1000);
        }
        if("OZ".equalsIgnoreCase(weightUnit) || "O".equalsIgnoreCase(weightUnit)){
            unifiedWeight = BigDecimal.valueOf(weight * 0.0283F);
        }
        return unifiedWeight.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * containerInfo
     */


    public static String aggregateContainerInfo(Map<String, List<String>> containerNoMap,
        Map<String, List<String>> containerSizeMap, Map<String, List<String>> firstSealNoMap,
        Map<String, List<String>> secondSealNoMap) {
        if (!CollectionUtils.isEmpty(containerNoMap)) {
            StringBuilder containerInfoStringBuilder = new StringBuilder();
            containerNoMap.forEach((String key, List<String> value) -> {
                String containerIndex = getIndexOfContainer(key);
                if(!StringUtils.isEmpty(containerIndex)){
                    String containerSizeKey = KEY_CONTAINER_SIZE.replace("\\d+",containerIndex);
                    String containerSeal1Key = KEY_CONTAINER_SEAL_NO1.replace("\\d+",containerIndex);
                    String containerSeal2Key = KEY_CONTAINER_SEAL_NO2.replace("\\d+",containerIndex);

                    List<String> containerSizeList = containerSizeMap.get(containerSizeKey);
                    List<String> containerSeal1List = firstSealNoMap.get(containerSeal1Key);
                    List<String> containerSeal2List = secondSealNoMap.get(containerSeal2Key);

                    IntStream.range(0,value.size()).forEach(index -> {
                        String containerNo = CollectionUtils.isEmpty(value) ? null : value.get(index);
                        String containerSize = CollectionUtils.isEmpty(containerSizeList) ? null : containerSizeList.get(index);
                        String containerSeal1 = CollectionUtils.isEmpty(containerSeal1List)  ? null : containerSeal1List.get(index);
                        String containerSeal2 = CollectionUtils.isEmpty(containerSeal2List) ? null : containerSeal2List.get(index);
                        if(!StringUtils.isEmpty(containerNo)){
                            containerInfoStringBuilder.append(containerNo).append("/").append(StringUtils.isEmpty(containerSize) ? 0 : NumberUtils
                                .parseNumber(containerSize,Integer.class)/100);
                            if(isNoSeal(containerSeal1)){
                                containerInfoStringBuilder.append("/").append(containerSeal1);
                            }
                            if(isNoSeal(containerSeal2)){
                                containerInfoStringBuilder.append("/").append(containerSeal2);
                            }
                            containerInfoStringBuilder.append(";");
                        }
                    });
                }
            });
            return format(containerInfoStringBuilder.toString());
        }
        return null;
    }

    private static String getIndexOfContainer(String containerNo){
        Matcher m = indexPattern.matcher(containerNo);
        if(m.find())
        {
            return m.group(1);
        }
        return null;
    }

    /**
     * goods descriptions
     */
    public static String aggregateGoodsDescription(List<String> containerNos_60, List<String> sequenceNos_60, List<String> descriptionText_60,
        List<String> containerNos_62, List<String> sequenceNos_62, List<String> descriptionText_62 ){
        Map<String,Map<String,String>> containerMap_60 = createContainerMap(containerNos_60, sequenceNos_60, descriptionText_60);
        Map<String,Map<String,String>> containerMap_62 = createContainerMap(containerNos_62, sequenceNos_62, descriptionText_62);

        if(!CollectionUtils.isEmpty(containerMap_60)){
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String,Map<String,String>> entry : containerMap_60.entrySet()){
                Map<String,String> sequenceMap = entry.getValue();
                String container = entry.getKey();
                stringBuilder.append(container).append(":").append((char)10);
                for(Map.Entry<String,String> sequenceDescEntry : sequenceMap.entrySet()){
                    String sequence = sequenceDescEntry.getKey();
                    String desc_60 = sequenceDescEntry.getValue();
                    if(StringUtils.isEmpty(desc_60) || "null".equals(desc_60)){
                        desc_60 = "";
                    }
                    stringBuilder.append((char)9)
                        .append(sequence)
                        .append(":")
                        .append(desc_60)
                        .append("").append((char)10);
                    if(!CollectionUtils.isEmpty(containerMap_62)){
                        Map<String,String> sequenceMap_62 = containerMap_62.get(container);
                        if(!CollectionUtils.isEmpty(sequenceMap_62)){
                            String desc_62 = sequenceMap_62.get(sequence);
                            if(StringUtils.isEmpty(desc_62) || "null".equals(desc_62)){
                                desc_62 = "";
                            }
                            stringBuilder.append((char)9)
                                .append(desc_62)
                                .append("").append((char)10);
                        }
                    }
                }
            }
            return format(stringBuilder.toString());
        }
        return null;
    }

    private static Map<String,Map<String,String>> createContainerMap(List<String> containerNos, List<String> sequenceNos, List<String> descText){
        if(!CollectionUtils.isEmpty(containerNos)){
            Map<String,Map<String,String>> containerMap = new HashMap<>(containerNos.size());
            int i = 0;
            for(String container : containerNos){
                String sequenceNo = sequenceNos.get(i);
                String sequenceDescription = descText.get(i);
                Map<String,String> sequenceDescriptionMap = containerMap.get(container);
                if(CollectionUtils.isEmpty(sequenceDescriptionMap)){
                    sequenceDescriptionMap = new HashMap<>();
                }else{
                    String existSequenceDescription = sequenceDescriptionMap.get(sequenceNo);
                    if(StringUtils.hasText(existSequenceDescription)){
                        sequenceDescription = existSequenceDescription + " " + sequenceDescription;
                    }
                }
                sequenceDescriptionMap.put(sequenceNo,sequenceDescription);
                containerMap.put(container,sequenceDescriptionMap);
                i++;
            }
            return containerMap;
        }
        return  null;
    }

    /**
     * marks descriptions
     */
    public static String aggregateMarks(List<String> containerNumber_80, List<String> marksAndNumbers_80){
        if(!CollectionUtils.isEmpty(containerNumber_80)){
            Map<String,String> marksMap = new HashMap<>(containerNumber_80.size());
            int i = 0;
            for(String containerNo : containerNumber_80){
                String existMarks = marksMap.get(containerNo);
                String marks = marksAndNumbers_80.get(i);
                if(!StringUtils.isEmpty(existMarks)){
                    marks = existMarks + "," + marks;
                }
                marksMap.put(containerNo, marks);
                i++;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for(Map.Entry<String,String>  entry : marksMap.entrySet()){
                String containerNo = entry.getKey();
                String marks = entry.getValue();
                if(StringUtils.isEmpty(marks) || "null".equals(marks)){
                    marks = "";
                }
                stringBuilder.append(containerNo)
                    .append(":").append((char)10)
                    .append(marks)
                    .append(";").append((char)10);
            }
            return format(stringBuilder.toString());
        }
        return null;
    }

    /**
     * multi column and multi container ,no repeat resolver.
     * refer : hsCode,containerNo
     */
    public static String aggregateMultiNoRepeatString(Map<String, List<String>> multiKeyValueMap) {
        Set<String> hsCodeSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(multiKeyValueMap)) {
            multiKeyValueMap.forEach((key, value) -> hsCodeSet.addAll(value));
            if (!CollectionUtils.isEmpty(hsCodeSet)) {
                StringBuilder sb = new StringBuilder();
                hsCodeSet.forEach(o -> {
                    if (StringUtils.hasText(o)) {
                        sb.append(o).append(";");
                    }
                });
                return format(sb.toString());
            }
        }
        return null;
    }

    public static String aggregateSealNo(Map<String, List<String>> sealNumberMap) {
        if (!CollectionUtils.isEmpty(sealNumberMap)) {
            StringBuilder sealNoStringBuilder = new StringBuilder();
            sealNumberMap.forEach((key, value) -> value.forEach(s -> {
                if(isNoSeal(s)){
                    sealNoStringBuilder.append(s).append(";");
                }
            }));
            return format(sealNoStringBuilder.toString());
        }
        return null;
    }

    private static boolean isNoSeal(String seal){
        String DOCUMENTAL = "DOCUMENTAL";
        String NO_SEAL = "NO SEAL";
        return  !StringUtils.isEmpty(seal) && !NO_SEAL.equals(seal) && !DOCUMENTAL.equals(seal);
    }

    private static boolean hasContainer(String containerNo){
        final String NO_CONTAINER = "NC";
        return !StringUtils.isEmpty(containerNo) && !NO_CONTAINER.equals(containerNo);

    }

    private static boolean hasContainerLength(String len){
        if(!StringUtils.isEmpty(len)){
            int containerLength = NumberUtils.parseNumber(len,Integer.class);
            return containerLength > 0;
        }
        return false;
    }

    private static String format(String value){
        if(!StringUtils.isEmpty(value) && value.endsWith(";")){
           return value.substring(0, value.length()-1);
        }
        return value;
    }

}
