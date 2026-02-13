
$port = 9000
$path = Get-Location

Write-Host "Starting HTTP Server on port $port..."
Write-Host "Serving files from: $path"
Write-Host "Open your browser and navigate to: http://localhost:$port/swagger-ui.html"
Write-Host "Press Ctrl+C to stop the server"
Write-Host ""

try {
    # PowerShell built-in HttpListener
    $listener = New-Object System.Net.HttpListener
    $listener.Prefixes.Add("http://localhost:$port/")
    $listener.Start()
    
    Write-Host "Server started successfully!"
    Write-Host "Documentation URL: http://localhost:$port/swagger-ui.html"
    
    while ($listener.IsListening) {
        $context = $listener.GetContext()
        $request = $context.Request
        $response = $context.Response
        
        $requestedFile = $request.Url.LocalPath.TrimStart('/')
        if ($requestedFile -eq "" -or $requestedFile -eq "/") {
            $requestedFile = "swagger-ui.html"
        }
        
        $filePath = Join-Path $path $requestedFile
        
        Write-Host "Request: $($request.Url.LocalPath) -> $requestedFile"
        
        if (Test-Path $filePath) {
            $content = Get-Content $filePath -Raw -Encoding UTF8
            $bytes = [System.Text.Encoding]::UTF8.GetBytes($content)
            
            # Set content type based on file extension
            $extension = [System.IO.Path]::GetExtension($filePath).ToLower()
            switch ($extension) {
                ".html" { $response.ContentType = "text/html; charset=utf-8" }
                ".yaml" { $response.ContentType = "text/yaml; charset=utf-8" }
                ".yml"  { $response.ContentType = "text/yaml; charset=utf-8" }
                ".css"  { $response.ContentType = "text/css; charset=utf-8" }
                ".js"   { $response.ContentType = "application/javascript; charset=utf-8" }
                ".json" { $response.ContentType = "application/json; charset=utf-8" }
                default { $response.ContentType = "text/plain; charset=utf-8" }
            }
            
            $response.ContentLength64 = $bytes.Length
            $response.StatusCode = 200
            $response.OutputStream.Write($bytes, 0, $bytes.Length)
        } else {
            $errorMessage = "404 - File Not Found: $requestedFile"
            $bytes = [System.Text.Encoding]::UTF8.GetBytes($errorMessage)
            $response.ContentType = "text/plain; charset=utf-8"
            $response.ContentLength64 = $bytes.Length
            $response.StatusCode = 404
            $response.OutputStream.Write($bytes, 0, $bytes.Length)
        }
        
        $response.OutputStream.Close()
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
} finally {
    if ($listener -and $listener.IsListening) {
        $listener.Stop()
        Write-Host "Server stopped."
    }
}