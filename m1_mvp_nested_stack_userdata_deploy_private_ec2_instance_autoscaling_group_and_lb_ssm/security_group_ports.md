# Instance Security Group Port Configuration

The EC2 instances in this CloudFormation template use a security group with the following inbound rules:

| Port | Protocol | Source IP Range | Purpose |
|------|----------|----------------|----------|
| 443  | TCP      | 0.0.0.0/0      | EC2 instance access via AWS Systems Manager (SSM) |
| 80   | TCP      | 0.0.0.0/0      | Web application traffic |

## Usage Context
- These ports are configured in the `InstanceSecurityGroup` resource
- The security group is attached to:
  1. EC2 instances launched via the Launch Template
  2. VPC Endpoints for AWS Systems Manager (SSM)
    - SSM Endpoint
    - SSM Messages Endpoint

## Network Architecture

```
VPC (10.0.0.0/16)
├── Public Subnet
│   └── Application Load Balancer
│       ├── Listens on Port 80 (HTTP)
│       └── Routes traffic to private instances
│
└── Private Subnet
    ├── EC2 Instances (Auto Scaling Group)
    │   ├── Security Group
    │   │   ├── Inbound Port 80: Web Traffic from ALB
    │   │   └── Inbound Port 443: SSM Access
    │   └── Docker Container (Port 80)
    │
    └── VPC Endpoints
        ├── SSM Endpoint (Port 443)
        └── SSM Messages Endpoint (Port 443)
```

## Web Traffic Flow

```
Internet
   │
   ▼
[Application Load Balancer] (public subnet)
   │                 Port 80/443 for web traffic
   ▼
[EC2 Instance(s)] (private subnet)
   │    ├── Security Group
   │    │   ├── Port 80: Web Traffic
   │    │   └── Port 443: SSM Access
   │    │
   │    └── Docker Container
         └── Port 80: Web Application

[SSM Endpoints] (private subnet)
   └── Port 443: Instance Management
```

1. Internet Traffic → Application Load Balancer (ALB)
   - External users access the application through the ALB
   - ALB is in public subnets and handles incoming HTTP traffic

2. ALB → EC2 Instances
   - ALB forwards requests to EC2 instances on port 80
   - Traffic is distributed across instances in the Auto Scaling Group
   - Instances are in private subnets, not directly accessible from internet

3. Container Application
   - Docker container running on EC2 listens on port 80
   - Container handles web requests and serves the application
   - Application logs are sent to CloudWatch via awslogs driver

## Security Considerations
- Port 443 is used for secure access to EC2 instances through AWS Systems Manager
- Port 80 is open for web traffic and is routed through the load balancer
- The instances are placed in a private subnet, with web traffic routed through a load balancer
- While both ports are open to 0.0.0.0/0, the private subnet placement provides an additional security layer
- Web traffic is isolated: users → ALB → private instances, never directly to EC2
