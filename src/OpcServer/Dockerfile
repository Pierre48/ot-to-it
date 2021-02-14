FROM mcr.microsoft.com/dotnet/runtime:5.0 AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src
COPY ["src/OpcServer/OpcServer.csproj", "src/OpcServer/"]
RUN dotnet restore "src/OpcServer/OpcServer.csproj"
COPY . .
WORKDIR "/src/src/OpcServer"
RUN dotnet build "OpcServer.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "OpcServer.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
COPY ["src/OpcServer/Quickstarts.ReferenceServer.Config.xml", "."]
ENTRYPOINT ["dotnet", "OpcServer.dll"]
