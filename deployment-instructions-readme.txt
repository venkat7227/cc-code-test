Ad Campaign App Deployment Instructions
=======================================
mvn clean install
(Clean Target->Compile sources->Compile Tests->Run Tests->Build War)

cp /adcampaign/target/adcampaign.war <App Server deploy directory>

start the <App Server> 