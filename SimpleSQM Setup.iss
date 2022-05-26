[Setup]
AppID={{85c45996-e7eb-4ff2-bd4a-4e7385acca5b}
AppName=SimpleSQM ASCOM ObservingConditions driver
AppVerName=SimpleSQM ASCOM ObservingConditions driver 1.0.2
AppVersion=1.0.2
AppPublisher=Marco Cipriani <marco.cipriani.01@gmail.com>
AppPublisherURL=mailto:marco.cipriani.01@gmail.com
AppSupportURL=https://marcocipriani01.github.io/
AppUpdatesURL=https://marcocipriani01.github.io/
VersionInfoVersion=1.0.2
MinVersion=6.1sp1
DefaultDirName="{commoncf}\ASCOM\ObservingConditions"
DisableDirPage=yes
DisableProgramGroupPage=yes
DisableReadyPage=yes
OutputDir="."
OutputBaseFilename="SimpleSQM Setup"
Compression=lzma
SolidCompression=yes
WizardStyle=modern
; Put there by Platform if Driver Installer Support selected
WizardImageFile="C:\Program Files (x86)\ASCOM\Platform 6 Developer Components\Installer Generator\Resources\WizardImage.bmp"
LicenseFile=".\LICENSE.txt"
; {commoncf}\ASCOM\Uninstall\ObservingConditions folder created by Platform, always
UninstallFilesDir="{commoncf}\ASCOM\Uninstall\ObservingConditions\SimpleSQM"
SetupIconFile=".\res\icon.ico"
UninstallDisplayIcon=".\res\icon.ico"

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Dirs]
Name: "{commoncf}\ASCOM\Uninstall\ObservingConditions\SimpleSQM"
; TODO: Add subfolders below {app} as needed (e.g. Name: "{app}\MyFolder")

[Files]
Source: ".\bin\Release\*"; DestDir: "{app}\ASCOM.SimpleSQM.ObservingConditions\"
Source: ".\LICENSE.txt"; DestDir: "{app}\ASCOM.SimpleSQM.ObservingConditions\"

; Only if driver is .NET
[Run]
; Only for .NET assembly/in-proc drivers
Filename: "{dotnet4032}\regasm.exe"; Parameters: "/codebase ""{app}\ASCOM.SimpleSQM.ObservingConditions\ASCOM.SimpleSQM.ObservingConditions.dll"""; Flags: runhidden 32bit
Filename: "{dotnet4064}\regasm.exe"; Parameters: "/codebase ""{app}\ASCOM.SimpleSQM.ObservingConditions\ASCOM.SimpleSQM.ObservingConditions.dll"""; Flags: runhidden 64bit; Check: IsWin64

; Only if driver is .NET
[UninstallRun]
; Only for .NET assembly/in-proc drivers
Filename: "{dotnet4032}\regasm.exe"; Parameters: "-u ""{app}\ASCOM.SimpleSQM.ObservingConditions\ASCOM.SimpleSQM.ObservingConditions.dll"""; Flags: runhidden 32bit; RunOnceId: "RemoveDDL1"
; This helps to give a clean uninstall
Filename: "{dotnet4064}\regasm.exe"; Parameters: "/codebase ""{app}\ASCOM.SimpleSQM.ObservingConditions\ASCOM.SimpleSQM.ObservingConditions.dll"""; Flags: runhidden 64bit; Check: IsWin64; RunOnceId: "RemoveDDL2"
Filename: "{dotnet4064}\regasm.exe"; Parameters: "-u ""{app}\ASCOM.SimpleSQM.ObservingConditions\ASCOM.SimpleSQM.ObservingConditions.dll"""; Flags: runhidden 64bit; Check: IsWin64; RunOnceId: "RemoveDDL3"

[Code]
const
   REQUIRED_PLATFORM_VERSION = 6.2;    // Set this to the minimum required ASCOM Platform version for this application

//
// Function to return the ASCOM Platform's version number as a double.
//
function PlatformVersion(): Double;
var
   PlatVerString : String;
begin
   Result := 0.0;  // Initialise the return value in case we can't read the registry
   try
      if RegQueryStringValue(HKEY_LOCAL_MACHINE_32, 'Software\ASCOM','PlatformVersion', PlatVerString) then 
      begin // Successfully read the value from the registry
         Result := StrToFloat(PlatVerString); // Create a double from the X.Y Platform version string
      end;
   except                                                                   
      ShowExceptionMessage;
      Result:= -1.0; // Indicate in the return value that an exception was generated
   end;
end;

//
// Before the installer UI appears, verify that the required ASCOM Platform version is installed.
//
function InitializeSetup(): Boolean;
var
   PlatformVersionNumber : double;
 begin
   Result := FALSE;  // Assume failure
   PlatformVersionNumber := PlatformVersion(); // Get the installed Platform version as a double
   If PlatformVersionNumber >= REQUIRED_PLATFORM_VERSION then	// Check whether we have the minimum required Platform or newer
      Result := TRUE
   else
      if PlatformVersionNumber = 0.0 then
         MsgBox('No ASCOM Platform is installed. Please install Platform ' + Format('%3.1f', [REQUIRED_PLATFORM_VERSION]) + ' or later from https://www.ascom-standards.org', mbCriticalError, MB_OK)
      else 
         MsgBox('ASCOM Platform ' + Format('%3.1f', [REQUIRED_PLATFORM_VERSION]) + ' or later is required, but Platform '+ Format('%3.1f', [PlatformVersionNumber]) + ' is installed. Please install the latest Platform before continuing; you will find it at https://www.ascom-standards.org', mbCriticalError, MB_OK);
end;

// Code to enable the installer to uninstall previous versions of itself when a new version is installed
procedure CurStepChanged(CurStep: TSetupStep);
var
  ResultCode: Integer;
  UninstallExe: String;
  UninstallRegistry: String;
begin
  if (CurStep = ssInstall) then // Install step has started
	begin
      // Create the correct registry location name, which is based on the AppId
      UninstallRegistry := ExpandConstant('Software\Microsoft\Windows\CurrentVersion\Uninstall\{#SetupSetting("AppId")}' + '_is1');
      // Check whether an extry exists
      if RegQueryStringValue(HKLM, UninstallRegistry, 'UninstallString', UninstallExe) then
        begin // Entry exists and previous version is installed so run its uninstaller quietly after informing the user
          Exec(RemoveQuotes(UninstallExe), ' /SILENT', '', SW_SHOWNORMAL, ewWaitUntilTerminated, ResultCode);
          sleep(1000);    //Give enough time for the install screen to be repainted before continuing
        end
  end;
end;

