## Test Settings

Hangi ortamda,hangi tarayıcıda çalışasağı test.properties dosyasında tanımlanır. Bu dosyayı ConfigManager classınaki methodlar okur.

headless=false -> headles mode aktif/inaktif

browser=chrome -> testlerin koşulacağı tarayıcı

testEnv=prod -> varsa test-prod ortamı seçimi (bu bilgiye göre UrlManager classında url döndülür)


## Testin Başlatılması

Aşağıdaki şekilde maven terminaline çalışacak test suite yazılıp run edilir. Veya direkt olarak testSuite folderı altındaki xml dosyaları çalıştırılır.

testSuite folderı altında run-all.xml veya farklı senaryo başlıkların bulunduğu xml runnerları bulunur.



## Elementlerin Tanımlanması

Elementler objectRepository folderı altında, json dosylarında tutulur. Her elementin type ve value değerleri vardır.

ElementManger classındaki methodlar bu json dosylarından elementi bulur, type değerine göre elementi oluşturur ve döndürür.


## Senaryoların Yazılması

Senaryoları testCases folderı altında classlara yazılır.
Elementleri string değeri olarak alır. Bu elementler yukarıda anlatıldığı gibi ElementManager ile bulunru ve oluşturulur. 


## Test Dataları

data.json dosyasından çekilir. Bu json dosyasını DataManger classındaki methodlar yardımıyla okur.
