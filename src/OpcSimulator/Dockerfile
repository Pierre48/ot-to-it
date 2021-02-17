FROM mcr.microsoft.com/dotnet/runtime:5.0 AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src
COPY ["src/OpcSimulator/OpcSimulator.csproj", "src/OpcSimulator/"]
RUN dotnet restore "src/OpcSimulator/OpcSimulator.csproj"
COPY . .
WORKDIR "/src/src/OpcSimulator"
RUN dotnet build "OpcSimulator.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "OpcSimulator.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
COPY ["src/OpcSimulator/OpcConfig.xml", "."]
ENTRYPOINT ["dotnet", "OpcSimulator.dll"]
